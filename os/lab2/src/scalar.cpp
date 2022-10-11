#include <bits/stdc++.h>
using namespace std;
#include "../include/matrix.h"
#include "../include/logger.h"

void print(vvl v)
{
    for(int i =0;i<v.size();i++)
    {
        for(int j =0;j<v[i].size();j++)
        {
            if(j<v[i].size()-1)cout<<v[i][j]<<",";
            else cout<<v[i][j];
        }
        cout<<endl;
    }
}

void add_scalar(ll num, vvl &v)
{
    char s1[] = "scalar addition started";
    logger(s1);
    for(int i =0;i<v.size();i++)
    {
        for(int j =0;j<v[0].size();j++)
        {
            v[i][j]+=num;
        }
    }

    print(v);
    char s2[] = "scalar addition successfully executed";
    logger(s2);
}

void sub_scalar(ll num, vvl &v)
{
    char s1[] = "scalar subtraction started";
    logger(s1);
    for(int i =0;i<v.size();i++)
    {
        for(int j =0;j<v[0].size();j++)
        {
            v[i][j]-=num;
        }
    }
    print(v);
    char s2[] = "scalar subtraction successfully executed";
    logger(s2);
}
void mul_scalar(ll num, vvl &v)
{
     char s1[] = "scalar multiplication started";
    logger(s1);
    for(int i =0;i<v.size();i++)
    {
        for(int j =0;j<v[0].size();j++)
        {
            v[i][j]*=num;
        }
    }
    print(v);
     char s2[] = "scalar multiplication successfully executed";
    logger(s2);
}

void div_scalar(ll num, vvl &v)
{

     char s1[] = "scalar division started";
    logger(s1);
    // if(num==0){cout<<"Runtime Error"<<endl;return;}
    for(int i =0;i<v.size();i++)
    {
        for(int j =0;j<v[0].size();j++)
        {
            v[i][j]/=num;
        }
    }
    print(v);
     char s2[] = "scalar division  successfully executed";
    logger(s2);
}