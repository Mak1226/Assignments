#include <bits/stdc++.h>
using namespace std;
#include "../include/matrix.h"
#include "../include/logger.h"
#include "../include/scalar.h"

void add_matrix(vvl &v1, vvl &v2)
{
    char s1[] = "Matrix addition started";
    logger(s1);
    vvl v=v1;
    for(int i =0;i<v1.size();i++)
    {
        for(int j =0;j<v1[0].size();j++)
        {
            v[i][j]=v1[i][j]-v2[i][j];
        }
    }
    print(v);
    char s2[] = "Matrix addition successfully executed";
    logger(s2);
}

void sub_matrix(vvl &v1, vvl &v2)
{
     char s1[] = "Matrix subtraction started";
    logger(s1);
    vvl v=v1;
    for(int i =0;i<v1.size();i++)
    {
        for(int j =0;j<v1[0].size();j++)
        {
            v[i][j]=v1[i][j]-v2[i][j];
        }
    }
    print(v);
     char s2[] = "Matrix subtraction successfully executed";
    logger(s2);
}

void mul_matrix(vvl &v1, vvl &v2)
{
     char s1[] = "Matrix multiplication started";
    logger(s1);
    vvl v;
    int n=v1.size();
    int m=v2.size();
    for(int i =0;i<n;i++)
    {
        vl v4(m);
        v.push_back(v4);
    }

    for(int i =0;i<v1.size();i++)
    {
        for(int j =0;j<v2[0].size();j++)
        {
            for(int k =0;k<v2.size();k++)
            {
                v[i][j]+=v1[i][k]*v2[k][j];
            }
        }
    }
    print(v);
     char s2[] = "Matrix multiplication successfully executed";
    logger(s2);
}

void transpose_matrix(vvl &v1)
{
    char s1[] = "Matrix transpose started";
    logger(s1);
    vvl v=v1;
    for(int i =0;i<v1.size();i++)
    {
        for(int j = 0;j<v1[0].size();j++)
        {
            v[i][j]=v1[j][i];
        }
    }
    print(v);
     char s2[] = "Matrix transpose successfully executed";
    logger(s2);
}


ll determinant(vector<vector<ll>>&matrix, int n) {
   ll det = 0;
   vector<vector<ll>>sub_matrix = matrix;
   if (n == 2)
   return ((matrix[0][0] * matrix[1][1]) - (matrix[1][0] * matrix[0][1]));
   else {
      for (int x = 0; x < n; x++) {
         int sub_i = 0;
         for (int i = 1; i < n; i++) {
            int sub_j = 0;
            for (int j = 0; j < n; j++) {
               if (j == x)
               continue;
               sub_matrix[sub_i][sub_j] = matrix[i][j];
               sub_j++;
            }
            sub_i++;
         }
         det = det + (pow(-1, x) * matrix[0][x] * determinant( sub_matrix, n - 1 ));
      }
   }
   return det;
}
