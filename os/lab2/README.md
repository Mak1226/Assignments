# LAB 2 EXPLANATION

Run the following command to run the **`LAB 2`**

```bash
$ make
$ make tests
$ make clean
```

- The `make` command will build the executable **`main`**
in the *`bin`* repo .
- Then running the `make tests` command will do the following:
    - It will execute the **`main`** with the appropiate test files 
    ( with ***.in*** extension ) in *`tests`* repo as input and build the 
    temporary output files in *`build`* repo . 
    - Then it will compare the corresponding output files with the expected
    output files (with ***.out*** extension ) in *`tests`* repo .
    - After comparing both the files the output displayed
    will be the conclusion whether the expected outputs and
    the generated outputs for a particular test case file
    is **SAME**  or **DIFFERENT** .
- Atlast the `make clean` command will delete the temporary files,
the executable, the object files, the statically linked and 
dynamically linked libraries and the log file in the *`build`* 
and *`lib`* repo .

`NOTE:` The actual work of running the test cases is done by the
`run.sh` .

&nbsp;

## `Makefile`
The makefile contains the following targets:

- **`build:`** The default target that will build the executable **`main`** 
in the *`bin`* repo .
- **`libs:`** This target will generate the **static library** and 
**dynamically linked library** in the *`lib`* repo .
- **`run:`** This target will run the executable on the *run.in* file
and compare the generated output with the *run.out* file and
gives the verdict.
- **`tests:`** This target will run the executable on the **5** test cases
in the *`tests`* repo and gives the verdict .
- **`test{1..5}:`** There are a total of **5** targets that will run the
corresponding test case in the *`tests`* repo and will give the appropiate
verdict .
- **`clean:`** This is the final target that will remove all the
files in the *`build`* and the *`lib`* repo .

## `Source Code`

The source code is spread across 2 repositories .

- *`src`* : It contains the **`C++`** source code
    
    - main.cpp
    - matrix.cpp
    - scalar.cpp
    - logger.cpp

<!-- &nbsp; -->

- *`include`* : It contains the **`header`** file code

    - logger.h
    - matrix.h
    - scalar.h

&nbsp;


### **`SRC`** 
- `main.cpp` is the main file whose executable will take
the input from the test files and will produce the output
to the output file .
- `matrix.cpp` contains all the functions related to the 
operations to be performed on the matrices .
- `scalar.cpp` contains all the functions related to the 
operations to be performed on the matrix and the scalar .
- `logger.cpp` will generate a ***log_file*** that will display
the logs such as the time when a particular test case runs
and whether an error has occcured while running the code.

### **`INCLUDE`**
- `matrix.h` is the header file for the matrix.cpp file
which contains all the function and vector definition .
- `scalar.h` is the header file for the scalar.cpp file
which contains all the function and vector definition .
- `logger.h` is the header file for the logger.cpp file
which contains the function definition .

### **`RUN.SH`**
- This `bash script` takes an integer argument between 
*{0..5}* to run a particular test case. 0 means the script 
will run on the run.in file and from 1 to 5 it means the 
script will run on test files in *`tests`* repo .
- First this file will add the *`lib`* repo to the path and 
generates a file called ***output*** in the *`build`* repo .
- Then this script compares the generated output and the 
expected output and displays an appropiate message for it.