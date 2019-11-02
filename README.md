# WorkManagerPeriodicRequest
A simple app that shows how to perform a periodic task using android WorkManager. It fetches data from a remote API, saves it in a Room database and displays the updated result in a recycler view. The PeriodicWorkRequest is scheduled to run every 15 minutes providing that the network constraint is satisfied and should there be any issue, WorkManager can retry the task using the specified retry policy.

_ _ _
## Medium Article
- [Periodic Tasks with Android WorkManager](https://medium.com/swlh/periodic-tasks-with-android-workmanager-c901dd9ba7bc)

- - -
### This project contains the following: 
- WorkManager
- Model View ViewModel (MVVM) Architecture
- Android Room Database
- ViewModel
- Retrofit
- LiveData
- Notification


