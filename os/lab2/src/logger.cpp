#include <bits/stdc++.h>
#include<ctime>
using namespace std;
#include "../include/logger.h"

    void logger(char* s)
    {
        FILE* p = fopen("log_file","a");
        time_t tm = time(NULL);
        fprintf(p,"%s \n%s\n",ctime(&tm), s);
        fclose(p);
    }

    




