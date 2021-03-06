package jp.covid19_kagawa.covid19information2.store


import jp.covid19_kagawa.covid19information2.action.ChartAction
import jp.covid19_kagawa.covid19information2.entity.InspectionData
import jp.covid19_kagawa.covid19information2.flux.Dispatcher
import jp.covid19_kagawa.covid19information2.flux.Store
import jp.covid19_kagawa.covid19information2.flux.StoreLiveData
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

class ChartStore(dispatcher: Dispatcher) : Store(dispatcher) {

    private val inspectionData = mutableListOf<InspectionData>()
    val loadingState = StoreLiveData<Boolean>()
    val loadedRepositoryListState = StoreLiveData<List<InspectionData>>()
    val inspectionNum = StoreLiveData<Int>()

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun on(action: ChartAction) {
        when (action) {

            is ChartAction.ShowLoading -> {
                Timber.tag(this::class.java.simpleName).d("action = ${action.isLoading}")
                loadingState.postValue(action.isLoading)
            }
            is ChartAction.FetchInfectData -> {
                inspectionData.clear()
                inspectionData.addAll(action.list)
                loadedRepositoryListState.postValue(inspectionData)
                inspectionNum.postValue(inspectionData.sumBy { it.count.toInt() })
            }
        }
    }

}