This assignment was developed using the RMI framework and implements the File sharing system

Be sure to open two command prompts 

First command prompt execute the below command
1.The below command is to start the server at port number 8000
java -cp pa2.jar Server start 8000

Second command prompt execute the below 

2.The below command will make a directory at the chosen path, here is an example of the path I gave
java -cp pa2.jar Client mkdir C:\Users\BHARAT\Desktop\testpa2 - make directory

3.The below command will upload the file to a given destination, it takes two arguments(from and to)
java -cp pa2.jar Client upload C:\Users\BHARAT\Desktop\tested\usda.ico C:\Users\BHARAT\Desktop\testpa2\ers.ico

4. The command 'dir' will list the files and folders present in the given path, below is an example
java -cp pa2.jar Client dir C:\Users\BHARAT\Desktop\testpa2

4. The command 'download' will download a file from source to destination and it takes two arguments(From and To)
java -cp pa2.jar Client download C:\Users\BHARAT\Desktop\testpa2\ers.ico C:\Users\BHARAT\Desktop\testpa2\new.ico

5. The command 'rm' will remove the file from the chosen directory and below is an example of the command
java -cp pa2.jar Client rm C:\Users\BHARAT\Desktop\testpa2\ers.ico

6. The command 'rmdir' will remove the directory and  below is an example
java -cp pa2.jar Client rmdir C:\Users\BHARAT\Desktop\testpa2

7. The command 'shutdown' will shutdown the server
java -cp pa2.jar Client shutdown
