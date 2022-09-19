#include <stdio.h>
#include <stdlib.h>
#include<signal.h>
//library for  execvp
#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/wait.h>
#include <string.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <sys/types.h>

//removing blank space
void blank_space_removal (char *s)
{
  if (s[strlen (s) - 1] == ' ' || s[strlen (s) - 1] == '\n')
  {
      s[strlen (s) - 1] = '\0';
  }
  while(s[0] == ' ' || s[0] == '\n')
  {
      memmove (s, s + 1, strlen (s));
  }
}

// separating string s using the delimiter c, and storing all the part in an array of strings in work
// and the size of the array in pointer np
void string_separator (char **work, const char *separator, int *np, char *s)
{
    char *str;
    str = strtok (s, separator);
    int p_counter = 0;
    while (str !=NULL)
    {
        work[p_counter] = malloc (sizeof (str) + 1);
        strcpy (work[p_counter], str);
        blank_space_removal (work[p_counter]);
        str = strtok (NULL, separator);
        p_counter++;
    }
  work[p_counter] = NULL;
  *np = p_counter;
}

//  executing external commands that are piped together
void piping_exec (char **work, int np)
{				
    //supporting up to 20 piped command task;
    if (np > 20)return;
    char *arg[200];
    int fd[20][2];
    int pc;
    int i =0;
    while(i < np)
    {
        string_separator (arg, " ", &pc, work[i]);
        if ((i != np - 1) && (pipe (fd[i]) < 0))
        {
            perror ("creation of pipe is  unsuccessfull\n");
            return;
        }

        int pid = fork ();
        if (pid == 0)
        {	
            //pid is zero means newly created child process.
            if (i != np - 1)
            {
                dup2 (fd[i][1], 1);
                close (fd[i][1]);
                close (fd[i][0]);
                
            }
            if (i != 0)
            {
                dup2 (fd[i - 1][0], 0);
                close (fd[i - 1][0]);
                close (fd[i - 1][1]);
                
            }
            execvp(arg[0], arg);
        }
        else if(pid<0)
        {
            //in case fork() is unsuccessfull, exit
            perror ("invalid input data");
            exit (1);	
        }

        //parent process
        if (i != 0)
        {			
            //closing of second process
            close (fd[i - 1][0]);
            close (fd[i - 1][1]);
        }
        wait(NULL);
        i++;
    }
}
//delcaring function to execute an external command
void executing_Basic_command(char **arg)
{
    int pid = fork();
    if (pid > 0)wait (NULL); //pid is position call returned to parent
    else if(pid ==0)execvp (arg[0], arg);//pid is zero means Returned to the newly created child process.
    else
    {
        //pid is negative, so creation of a child process was unsuccessful.
        perror ("invalid input data" "\n");
        exit (1);
    }
}

//  executing external commands that have to be run asyncronously
void background_Executing (char **work, int np)
{
    int pc;
    char *arg[100];
    int i =0;
    while(i < np)
    {
        string_separator (arg, " ", &pc, work[i]);
        int pid = fork();
        if (pid == 0)execvp (arg[0], arg);
        else if(pid<0)
        {
            //in case fork() is unsuccessfull, exit
            perror ("invalid input data");
            exit (1);	
        }
        i++;
    }
    i=0;
    while(i < np)
    {
      wait (NULL);
      i++;
    }
}
//  executing an external command that needs file redirection(i.e input, output or append)
void exec_IO (char **work, int np, int mode)
{
    int fd,pc;
    char *arg[200];
    blank_space_removal (work[1]);
    string_separator (arg, " ", &pc, work[0]);
    int pid=fork();
   if(pid==0)
   {
        if(mode==0)fd = open (work[1], O_RDONLY);
        else if(mode==1)fd = open (work[1], O_WRONLY);
        else if(mode==2)fd = open (work[1], O_WRONLY | O_APPEND);
        else if(mode==3)fd = open (work[1], O_WRONLY);
        else return;

        if (fd < 0)
        {
            perror ("can not open file\n");
            return;
        }

        if(mode==0 ||mode==1)dup2 (fd, mode);
        else if(mode==2 || mode ==3)dup2 (fd, mode-1);
        else return;
        
        execvp (arg[0], arg);
    }
    else if(pid <0)
    {
        //in case fork() is unsuccessfull, exit
        perror ("invalid input data");
        exit (1);		
    }
    wait (NULL);
}
//showing normal message
void Help ()
{
  printf ("You can try something else\n");
}


int main (char **arg, int argc)
{
    char work[1000],cwd[1025];
    char *str, *works2[100],*works1[200],*buff[200];
    int np = 0;
    printf ("SHAILAB'S CUSTOM SHELL" "\n");    
    printf (" =========== SHELL ===========" "\n");

    int pid = getpid();
    while (1)
    {
        //printing current Directory
        if (getcwd(cwd, sizeof (cwd)) != NULL)printf ("%s $ ", cwd);
        else perror("getcwd failed\n");

        //reading input
        fgets(work, 1000, stdin);	//buff overflow can not happen

        //check if there is no pipe , redirection , background
        if (strstr(work, "<<<"))
        {			
            //redirecting file to input
            string_separator(buff, "<<<", &np, work);
            if (np == 2) exec_IO (buff, np, 0);
            else printf ("error ,type in correct syntax\n");
        }
        else if (strstr(work, "||"))
        {	//divide pipe commands
            string_separator (buff, "||", &np, work);
            piping_exec (buff, np);
        }
        else if (strstr (work, ">>>"))
        {
            string_separator (buff, ">>>", &np, work);
            if(np == 2) exec_IO (buff, np, 2);
            else printf ("error ,type in correct syntax\n");
        }
        else if(strstr(work, "&>>"))
        {
            string_separator (buff, "&>>", &np, work);
            if(np == 2) exec_IO (buff, np, 3);
            else printf ("error ,type in correct syntax\n");
        }
        else if (strchr (work, '&'))
        {			
            //background execution
            string_separator (buff, "&", &np, work);
            background_Executing (buff, np);
        }
        else
        {
            string_separator (works1, " ", &np, work);
            if (strstr (works1[0], "cd"))
            {	//cd command
                if(works1[1]==NULL)chdir("/home");
                else chdir (works1[1]);
            }
            else if (strstr (works1[0], "exit"))exit (0);//exiting
            else if (strstr (works1[0], "help"))Help ();//help command
            else executing_Basic_command (works1);	// rest commands 
        }
    }

  return 0;
}