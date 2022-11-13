# Implementation of `FlexSC` in **XV6**
____
&emsp;
## **◽** What is FlexSC?

FlexSC is an ***`implementation of exceptionless system calls`*** in the Linux kernel, 
and an accompanying user-mode thread package *(FlexSC-Threads)*, 
binary compatible with POSIX threads, that translates legacy
synchronous system calls into exceptionless ones transparently to applications.

&emsp;

## **◽** Advantages of FlexSC

They **`improve processor effciency`** by enabling flexibility in the scheduling of operating system work, 
which in turn can lead to significantly increased temporal and spacial locality of execution in both
user and kernel space, thus **`reducing pollution effects`** on processor structures. 

&emsp;

## **◽** Execution

First of all, we'll implement threads in *XV6*, as using the threads will speed up the process.
Without the threads, the entire address space would have to be copied.
Since these threads share the same address space, they will load quickly, 
therefore we'll clone a syscall thread for each system call.
And we use the concept of core specialisation and use *`specific core`* to execute all the syscall threads.

&emsp;

## **◽** Changes in the code

- [Threading Library](https://github.com/avivmag/XV6-Kernel-Level-Threads-Synchronization-And-Memory-Management) :
    > We'll use the above library, but will make some suitable changes.

<div style="margin-top:2.5%">

- `Minor` `Modification` : 
    > We'll add an entry `sycall entry id` to **`struct proc`** in *`proc.h`* to keep track of the syscall id
    and `registered` entry to know if the process has called *`flexsc_register()`*.

<div style="margin-top:2.5%">  

- `Minor` `Modification` : 
    > A syscall thread can be identified by an entry in the thread struct, and only a *`specific core`* will be able to execute it.

<div style="margin-top:2.5%">

- `Minor` `Modification` : 
    > In scheduler, we set a flag to 1 in case no process is **RUNNABLE**, therefore calling *`flexsc_wait()`*.

<div style="margin-top:2.5%">

- `Feature` :
    > We'll create a new struct **`syscall page`** in *`proc.c`* to store the syscall entries 
    which acts as shared memory, hence the syscall thread created will also have this entry.

  - _Data_ _Structure_ :
  
   <div style="border:1.5px solid green; margin-right:40%; margin-left:5%; margin-top:3%; margin-bottom:3%">

    ```c
    struct syscall_entry 
    {
        int syscall_num;
        int narg;
        int status;
        int arg[10];
        int ret_val;
    };

    struct 
    {
        struct spinlock lock;
        struct syscall_entry entry[NSYSCALL_ENTRY];
    } syscall_page;
    ```
    </div>

- `Feature` : 
    > To support exceptionless system calls, we'll change **`syscall`** in *`syscall.c`*.
  
  - _Algorithm_ :

  <div style="border:1.5px solid green; margin-right:40%; margin-left:5%; margin-top:2%; margin-bottom: 2%">
  
        If the process is FLEXSC_REGISTERED, then
            ◽ we create a syscall thread.
            ◽ come out of the process into the scheduler.
            ◽ we allocate a new syscall entry.

        If not, then the same synchronous syscall.

    </div>

<div style="margin-top:2.5%">

- `Test Cases` :
    > We'll compare how long system calls take in both implementations. To assess performance, we use sample system calls.
