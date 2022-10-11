#ifndef Matrix_H
#define Matrix_H
#include <bits/stdc++.h>
using namespace std;
typedef vector<int> vi;
typedef vector<vi> vvi;
typedef pair<int,int> pii;
typedef long long int ll;
typedef vector < ll > vl;
typedef vector < char > vc;
typedef vector < vl > vvl;
void add_matrix(vvl &v1, vvl &v2);
void sub_matrix(vvl &v1, vvl &v2);
void mul_matrix(vvl &v1, vvl &v2);
void transpose_matrix(vvl &v1);
ll determinant (vvl & v,int n);
#endif