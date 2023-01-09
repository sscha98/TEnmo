# Module 2 Capstone - TEnmo (Pair programming assignment for TECH ELEVETOR)
1. A user of the system can register with a username and password.
2. A user of the system can log in with registered username and password.
   1. User ids start at 1001.
3. As a user, when registered, a new account is created
   1. The new account has an initial balance of $1000.
   2. Account ids start at 2001.
4. As an authenticated user of the system, the user can view Account Balance.
5. As an authenticated user of the system, the user can *send* a transfer of a specific amount of TE Bucks to a registered user.
   1. Users can choose from a list of users to send TE Bucks to.
   2. Users can not send funds to themselves.
   3. A transfer includes the User IDs of the from and to users and the amount of TE Bucks.
   4. The receiver's account balance is increased by the amount of the transfer.
   5. The sender's account balance is decreased by the amount of the transfer.
   6. Users are not able to send more TE Bucks than the amount in their account balance.
   7. A zero or negative amount can not be sent or received
   9. Transfer ids start at 3001.
6. As an authenticated user of the system, the user can view previous transfers (Sent or Received funds)
7. As an authenticated user of the system, the user can retrieve the details of any transfer based upon the transfer ID.
