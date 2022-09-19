
# 1ST QUESTION 


## The file explains the working of the code

Run the following bash script file for 1st Question :

```bash
  $ bash q1.sh
```


## q1.sh
The script runs these bash files :

``` bash
    bash init.sh
    bash 1.sh
    bash 2.sh
    bash 3.sh
    bash 4.sh
    bash 5.sh

```
- NOTE : some of these scripts also take input arguments.


## Explanation

- **init.sh** file clears any previous data in all *txt* files.

- In 1st question I am printing the tags appropriately i.e., ***rollno1_rollno2*** for students in pair & ***rollno*** for single student and writing all the rollno. in single line to *d.txt* . This is done by bash script **1.sh** and tags are stored in *z.txt* . 

- Then in bash script **2.sh** I am sorting the *z.txt* file and using piplining I am printing the unique tags in *a.txt* . After that all that rollno. in *d.txt* are sorted and through piplining I am finding the duplicate rollno. from sorted file and redirecting the ***stdout***(duplicate rollno.) to ***stderr*** file *b.txt* . 

- The bash script **3.sh** takes the rollno. from *b.txt*(duplicate rollno.) and removes all tags associated with that rollno. and writes them in *c.txt* . Hence *c.txt* has all ***UNIQUE & VALID*** tags. 

- The bash script **4.sh** writes the rollno. of those students who have not filled the form but are registered in course to file *e.txt* . 

- The bash script **5.sh** writes the tags of any student who isn't registered to the course to file *f.txt* . 
