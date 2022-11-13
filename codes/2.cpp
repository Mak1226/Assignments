#include <bits/stdc++.h>
using namespace std;
int main()
{
    stack <char> s;
    string str;
    cin >> str;
    bool f = true;
    
    for( auto &i : str)
    {
        if(i == '(')
            s.push(i);
        else if (i == ')')
            if(s.empty())
                f = false;
            else
                s.pop();
    }
    for (int i = 0; i < str.length(); i++)
    {
        if(str[i] == '(')
            s.push(str[i]);
        else if (str[i] == ')')
            if(s.empty())
                f = false;
            else
                s.pop();
    }
    if(f && s.empty())
        cout << "BALANCE" << endl;
    else
        cout << "UNBALANCE" << endl;
}