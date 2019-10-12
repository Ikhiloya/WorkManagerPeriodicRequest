# WorkManagerPeriodicRequest
A simple app that shows how to perform a periodic task using android WorkManager. It fetches data from a remote API, saves it in a Room database and displays the updated result in a recycler view. The PeriodicWorkRequest is scheduled to run every 15 minutes providing that the network constraint is satisfied and should there be any issue, WorkManager can retry the task using the specified retry policy.  

### This project contains the following: 
##### WorkManager
##### Model View ViewModel (MVVM) Architecture
##### Android Room Database
##### ViewModel
##### Retrofit
##### LiveData
##### Notification
