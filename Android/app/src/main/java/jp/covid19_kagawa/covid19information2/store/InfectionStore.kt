package jp.covid19_kagawa.covid19information2.store

import android.text.Spannable
import android.text.style.RelativeSizeSpan
import android.text.style.UnderlineSpan
import jp.covid19_kagawa.covid19information2.action.InfectionAction
import jp.covid19_kagawa.covid19information2.entity.InfectionSummary
import jp.covid19_kagawa.covid19information2.entity.SummaryEntity
import jp.covid19_kagawa.covid19information2.flux.Dispatcher
import jp.covid19_kagawa.covid19information2.flux.Store
import jp.covid19_kagawa.covid19information2.flux.StoreLiveData
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber


class InfectionStore(dispatcher: Dispatcher) : Store(dispatcher) {
    companion object {
        private const val INITIAL_PAGE = 1
        private const val MAIN_TITLE = "累計患者数"
        private const val INFECTED_TITLE = "陽性患者数"
        private const val HOSPITAL_TITLE = "入院患者数"
        private const val LIGHT_TITLE = "軽症・中等症"
        private const val HEAVY_TITLE = "重症"
        private const val RECOVERED_TITLE = "退院"
        private const val DIED_TITLE = "死亡"
        private const val SUB_TITLE = "（累計）"
        private const val SUB_APPEND = "人"
        private const val HOME_MAIN_TEXT = " の情報を表示中"
        private const val HOME_MAIN_HEAD = "現在 "
    }

    var canFetchMore = false
        private set

    var pageNum = INITIAL_PAGE
        private set

    private val loadingInfectionList = mutableListOf<SummaryEntity>()
    val loadingState = StoreLiveData<Boolean>()
    val loadedInfectionData = StoreLiveData<List<SummaryEntity>>()
    val currentPrefectureName = StoreLiveData<Spannable>()

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun on(action: InfectionAction) {
        when (action) {

            is InfectionAction.ShowLoading -> {
                Timber.tag(this::class.java.simpleName).d("action = ${action.isLoading}")
                loadingState.postValue(action.isLoading)
            }
            is InfectionAction.FetchInfectionData -> {
                loadingInfectionList.clear()
                loadingInfectionList.addAll(makeInfectionList(action.data))
                loadedInfectionData.postValue(loadingInfectionList)
            }

            is InfectionAction.GetCurrentPrefectureNameAction -> {
                currentPrefectureName.postValue(makeText(action.prefName))
            }

        }
    }

    private fun makeText(baseStr: String): Spannable {
        val tmpStr = HOME_MAIN_HEAD + baseStr + HOME_MAIN_TEXT
        val spannable = Spannable.Factory.getInstance().newSpannable(tmpStr)

        val underlineSpan = UnderlineSpan()
//        val styleSpan = StyleSpan(Typeface.BOLD)
        val span = RelativeSizeSpan(1.5f)
        spannable.setSpan(
            underlineSpan,
            HOME_MAIN_HEAD.length,
            baseStr.length + HOME_MAIN_HEAD.length,
            spannable.getSpanFlags(underlineSpan) // getSpanFlagsの引数に、引数1のインスタンスを指定してください
        )
//        spannable.setSpan(
//           styleSpan,
//            0,
//            baseStr.length,
//            spannable.getSpanFlags(styleSpan) // getSpanFlagsの引数に、引数1のインスタンスを指定してください
//        )
        spannable.setSpan(
            span,
            HOME_MAIN_HEAD.length,
            baseStr.length + HOME_MAIN_HEAD.length,
            spannable.getSpanFlags(span) // getSpanFlagsの引数に、引数1のインスタンスを指定してください
        )
        return spannable
    }

    private fun makeInfectionList(infectionSummary: InfectionSummary): List<SummaryEntity> {

//        val inspectedEntity = InfectionEntity(
//            MAIN_TITLE,
//            SUB_TITLE,
//            infectionSummary.inspection_num.toString(),
//            ""
//        )

        //陽性患者数
        val infectedEntity = SummaryEntity(
            INFECTED_TITLE,
            SUB_TITLE,
            infectionSummary.infect_num.toString() + SUB_APPEND,
            ""
        )

        //入院中
        val hospitalEntity = SummaryEntity(
            HOSPITAL_TITLE,
            SUB_TITLE,
            infectionSummary.infect_hospital.toString() + SUB_APPEND,
            ""
        )

        //軽症
        val lightEntity = SummaryEntity(
            LIGHT_TITLE,
            SUB_TITLE,
            infectionSummary.infect_light.toString() + SUB_APPEND,
            ""
        )

        //重症
        val heavyEntity = SummaryEntity(
            HEAVY_TITLE,
            SUB_TITLE,
            infectionSummary.infect_heavy.toString() + SUB_APPEND,
            ""
        )

        //死亡
        val diedEntity = SummaryEntity(
            DIED_TITLE,
            SUB_TITLE,
            infectionSummary.infect_died.toString() + SUB_APPEND,
            ""
        )

        //退院
        val recoverEntity = SummaryEntity(
            RECOVERED_TITLE,
            SUB_TITLE,
            infectionSummary.inafect_recover.toString() + SUB_APPEND,
            ""
        )
        return mutableListOf(
            //      inspectedEntity,
            infectedEntity,
            hospitalEntity,
            lightEntity,
            heavyEntity,
            diedEntity,
            recoverEntity
        )
    }
}