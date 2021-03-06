package jp.covid19_kagawa.covid19information2.store


import jp.covid19_kagawa.covid19information2.action.ContactAction
import jp.covid19_kagawa.covid19information2.entity.ContactEntry
import jp.covid19_kagawa.covid19information2.flux.Dispatcher
import jp.covid19_kagawa.covid19information2.flux.Store
import jp.covid19_kagawa.covid19information2.flux.StoreLiveData
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

class ContactStore(dispatcher: Dispatcher) : Store(dispatcher) {

    private val contactData = mutableListOf<ContactEntry>()
    val loadingState = StoreLiveData<Boolean>()
    val loadedRepositoryListState = StoreLiveData<List<ContactEntry>>()
    val contactNum = StoreLiveData<Int>()
    val contactDate = StoreLiveData<String>()

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun on(action: ContactAction) {
        when (action) {
            is ContactAction.ShowLoading -> {
                Timber.tag(this::class.java.simpleName).d("action = ${action.isLoading}")
                loadingState.postValue(action.isLoading)
            }
            is ContactAction.FetchContactData -> {
                contactData.clear()
                contactData.addAll(action.data.entries)
                loadedRepositoryListState.postValue(contactData)
                contactDate.postValue(action.data.date)
                contactNum.postValue(contactData.sumBy { it.value })
            }
        }
    }

}