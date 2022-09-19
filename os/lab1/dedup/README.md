
# 2ND QUESTION 


## The file explains the code

Run the following script file for 2nd Question :

```bash
  $ bash q2.sh
```

## q2.sh
The script runs these bash files :

``` bash
    bash dedup.sh
    bash dedup.sh inputs/
    bash dedup.sh inputs/old/
```

NOTE : The script runs the  **dedup.sh** script in *3* directories. The directories are the input arguments to the **dedup.sh** script.

- current directory
- inputs/
- inputs/old/

## dedup.sh

The script does the following :

- first it checks if there are any input arguments (directory). If there are any, then it will change into that directory and then it creates a ***list.txt*** file which stores all the ***FILES*** and neglect any directory, links and also the ***list.txt*** file itself.

- After that I am storing no. of lines in a variable and running the loop. The loops reads a specific line from the ***list.txt*** and checks if it's a file (& also not a link) & stores it in a variable *$files* . The inner loop reads the next line from ***line.txt*** and checks if its a file(& not also not a link) & stores it in *$copy* .

- Then using the **cmp** command I am checking if the two files in variables *$files* & *$copy* are same or not. If the files are same then I remove the *$copy* and create a **link** to the original file. Atlast I remove the ***list.txt*** file.
