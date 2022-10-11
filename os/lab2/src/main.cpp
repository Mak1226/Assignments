#include <bits/stdc++.h>
#include "../include/matrix.h"
#include "../include/logger.h"
#include "../include/scalar.h"
using namespace std;
#include<unistd.h>

typedef vector<int> vi;
typedef vector<vi> vvi;
typedef pair<int,int> pii;
typedef long long int ll;
typedef vector < ll > vl;
typedef vector < char > vc;
typedef vector < vl > vvl;
typedef vector < vc > vvc;
typedef vector < pii > vp;

vl separator(char* s1)
{
    vl v;
    char* ptr=strtok(s1," ,");
    while(ptr!=NULL)
    {
        v.push_back(atoi(ptr));
        ptr=strtok(NULL, " ,");
    }

    return v;
}
int main(int arg, char * argv[])
{
    string s;
    vvl v;

    getline(cin,s);
    vector<vector<char>> argv1(1);

    int argc=0;
    if(s.size()>=3 && s[2] == '-')
    {
        if(s.size() >=4)argc=3; 
        argv1[0].push_back(s[2]);
        argv1[0].push_back(s[3]);
    }
    else
    {
        argc = arg;
        argv1[0].push_back(argv[1][0]);
        argv1[0].push_back(argv[1][1]);
    }
    while(getline(cin, s))
    {
        char s1[s.size()];
        for(int i =0;i<s.size();i++)s1[i]=s[i];
        if(s.empty()||s1[0]=='#')continue;
        vl v1 = separator(s1);
        v.push_back(v1);
    }


    // for(int i =0;i<v.size();i++)
    // {
    //     for(int j = 0;j<v[i].size();j++)
    //     {
    //         cout<<v[i][j]<<" ";
    //     }
    //     cout<<endl;
    // }
    
    // cout<<argv[1][1]<<endl;
    int key=0;
    if(argv1[0][0] == '-' && (argv1[0][1] == 'a' || argv1[0][1] == 's' ||argv1[0][1] == 'm' ||argv1[0][1] == 'd')) 
    {
        // cout<<111<<endl;
        ll num;
        vvl v1;
        if(v[0].size()> 2)key=1;
        else if(v[0].size()==1)
        {
            num=v[0][0];
            int n=v[1][0];
            if(v[1].size()!=2)key=1;
            else if(v.size()<n+1)key=1;
            else
            {
                for(int i =2;i<n+2;i++)
                {
                    if(v[i].size()!=v[1][1])
                    {
                        key=1;
                        break;
                    }
                    v1.push_back(v[i]);
                }
            }
            
        }
        else
        {
            int n = v[0][0];

            if(v.size()<= n+1)key=1;
            else
            {
                for(int i =1;i<=n;i++)
                {
                    if(v[i].size()!=v[0][1])
                    {
                        key=1;
                        break;
                    }
                    v1.push_back(v[i]);
                }

                if(v[n+1].size()!=1)key=1;
                else num = v[n+1][0];
            } 
        }

        if(key ==1)
        {
            char s[] = "Invalid input for the given operation";
            logger(s);
        }
        else if(argv1[0][1] =='a' ){add_scalar(num,v1);}
        else if(argv1[0][1] == 'm' ) mul_scalar(num,v1);
        else if(argv1[0][1] == 's' ) sub_scalar(num,v1);
        else
        {
            if(num==0)
            {
                char s[] = "Runtime Error, Division by 0.";
                logger(s);
            }
            else
            {
                div_scalar(num,v1);
            }

        } 
    }
    else if(argv1[0][0] == '-' && (argv1[0][1] == 'A' || argv1[0][1] == 'S' || argv1[0][1] == 'M'))
    {
        vvl v1,v2;
        ll n=v[0][0];
        int i=1;
        if(v[0].size()!=2)
        {
            key=1;
        }
        else if(v.size()<= n+1)key=1;
        else
        {
            while(i<=n)
            {
                if(v[0][1] != v[i].size())
                {
                    key=1;
                    break;
                }
                v1.push_back(v[i]);
                i++;
            }
        }
       
        n+=v[i][0]+1;
        int j =i;
        if(v[i].size()!= 2)
        {
            key=1;
        }
        else if(v.size()<n) key=1;
        else
        {
            i++;
            while(i<=n)
            {
                if(v[j][1] !=v[i].size())
                {
                    key=1;
                    break;
                }
                v2.push_back(v[i++]);
            }
        }
        
        if(key==1)
        {
            char s[] = "Invalid input for the given operation.";
            logger(s);
        }
        else if(argv1[0][1] =='A' )
        {
            if(v1.size() == v2.size() && v1[0].size() == v2[0].size())add_matrix(v1,v2);
            else
            {
                char s[] = "Dimensions of both matrix are different, therefore matrix addition isn't possible.";
                logger(s);
            }
        }
        else if(argv1[0][1] == 'M' )
        {
            if(v1[0].size() == v2.size())mul_matrix(v1,v2);
            else 
            {
                char s[] = "Invalid dimensions for matrix multiplication, therefore multiplication isn't possible.";
                logger(s);
            }
        } 
        else if(argv1[0][1] == 'S' )
        {
            if(v1.size() == v2.size() && v1[0].size() == v2[0].size())sub_matrix(v1,v2);
            else
            {
                char s[] = "Dimensions of both matrix is different, therefore matrix subtraction isn't possible.";
                logger(s);
            }
        } 
    }
    else if(argv1[0][0] == '-' && (argv1[0][1] == 'T' || argv1[0][1] == 'X'))
    {
        vvl v1;
        int n=v[0][0];
        int i=1;
        if(v[0].size() !=2)key=1;
        else if( v[0][0] != v[0][1])key=1;
        else if(v.size()<=n)key=1;
        else
        {
            while(i<=n)
            {
                if(v[i].size() != n)
                {
                    key=1;
                    break;
                }
                v1.push_back(v[i]);
                i++;
            }
        }
        if(key==1)
        {
            char s[] = "Invalid input for the required operation.";
            logger(s);
        }
        else if(argv1[0][0] == '-' && argv1[0][1] == 'T')transpose_matrix(v1);
        else if(argv1[0][0] == '-' && argv1[0][1] == 'X')
        {
            char s[] = "Determinant calculation started";
            logger(s);
           cout<<determinant(v1,n)<<endl;
            char s1[] = "Determinant calculation successful";
            logger(s1);
        }
    }
    else 
    {
        char s1[] = "Invalid Command given";
        logger(s1);
    }
    if(argc >2)
    {
        char s1[] = "Terminating the program after executing first command and ignoring the rest commands(if any)";
        logger(s1);
    }
    return 0;
}
