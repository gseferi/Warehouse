Team Assignment 3: An Autonomous Warehouse
=============
Here is the assignment link from Canvas : 
https://canvas.bham.ac.uk/courses/15668/assignments/51114

*This project is for PC side, click [HERE](https://git.cs.bham.ac.uk/serobot-1.2/robot) to browse the Robot side.

Branch operations
-----------
0. Please do not work directly on master branch as to avoid data loss when merging.

0. To create a new branch, type `git branch branchname` to create a new branch

0. To switch to a branch, type `git checkout branchname` to switch a specified branch

0. To update the code in your branch, type `git pull origin master` to update the codes in the master branch, then type `git merge master` to merge the code. Then switch back to master branch to merge your code to master and push. As to avoid merge conflicts and push it to the repository as soon as possible.

Set Up 
-----------
0. Open GitBash, type `cd e:/` to go to the directory you would like to pull the project `(e:/ in here as an example)`.
    ```
    cd e:/
    ```
    
0. type `git clone https://git.cs.bham.ac.uk/serobot-1.2/warehouse.git`
    ```
    git clone https://git.cs.bham.ac.uk/serobot-1.2/warehouse.git
    ```
Upload your changes
-----------
0. Open GitBash and go to the directory where you have edited the files
    
    ```
    cd e:/warehouse
    ```
    
0. type `git add filename` to add the file you do changes
    ```
    git add yourfilename
    ```
Or, type `git add .` to add all the changed files
    ```
    git add .
    ```
0. type `git commit -a -m "Your commit message"` to commit your changes 
    ```
    git commit -a -m "some brief discriptions"
    ```
    
0. type `git push` to push file to GitLab
    ```
    git push 
    ```

Task classification
-------
0. Team Management : Isa
0. Integration : Raymond,Emil
0. Robot Interface : Aaquib, Isa, Bobby
0. Robot Motion Control : Alexandra, Pat(Interface also)
0. Job Selection : Hussien, Pat, Gkerta
0. Job Assignment : Hussien, Pat, Gkerta
0. Warehouse Management Interface : Aaquib, Isa, Bobby
0. (Multi-Robot) Route Planning : Evan, Emil
0. Route Execution : Evan
0. Network Communication : Thomas and Raymond
0. Localisation : Alexandra, Pat, Emil, Evan
