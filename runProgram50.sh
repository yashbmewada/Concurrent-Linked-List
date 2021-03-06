#!/bin/bash 
#SBATCH -J YBMListTest # job name 
#SBATCH -o YBM_50_%j.csv # output and error file name (%j=jobID) 
#SBATCH -N 5 # number of nodes to run on
#SBATCH -n 48 # total number of cpus requested 
#SBATCH -p development # queue -- normal, development, etc. 
#SBATCH -t 01:30:00 # run time (hh:mm:ss) - 1.5 hours 
#SBATCH --mail-user=ybm170030@utdallas.edu
#SBATCH --mail-type=begin # email me when the job starts 
#SBATCH --mail-type=end # email me when the job finishes
cd bin
java Main 50 350 # run the Java program  - Args [0] - Number of threads and Args[1] - Number of Bound on count
