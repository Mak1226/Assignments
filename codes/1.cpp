#include <bits/stdc++.h>
using namespace std;
void print(vector <int> &v)
{
    for (int i = 0; i < v.size(); i++)
    {
        cout << v[i] << " " << endl;
    }
    
}
void print(vector <pair<int,int>> &v)
{
    for (int i = 0; i < v.size(); i++)
    {
        cout << v[i].first << " " << v[i].second << endl;
    }
    
}
int main()
{
    vector < pair <int,int>> v = {{1,1},{2,2},{3,3}};   //make_pair(4,4) otherwau to make pair
    v.push_back({4,4});
    cout << "pair" << endl;
    print(v);
    v.pop_back();
    print(v);
    cout << "int" << endl;
    vector <int> x;
    for (int i = 1; i < 5; i++)
        x.push_back(i);
    print(x);
    x.pop_back();
    print(x);
    int a[] = {1,2,3,4,5};
    for(int i : a)                  //value of a is COPIED into i | use &i for reference
        cout << i << " " << endl;
    cout << "set" << endl;
    set <int> s = {1,2,3,4,5};
    s.erase(5);
    for(auto v : s)
        cout << v << endl;
}