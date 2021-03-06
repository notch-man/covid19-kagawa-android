package jp.covid19_kagawa.covid19information2.repository

import jp.covid19_kagawa.covid19information2.GuideSelection
import jp.covid19_kagawa.covid19information2.Prefecture
import jp.covid19_kagawa.covid19information2.data.repository.PreferenceRepository

class GuideRepository(
    private val preferenceRepository: PreferenceRepository
) {

    fun selectTitleMessage(selection: GuideSelection) = selection.selectGuideSceneTitle()
    fun getCurrentWebLink() =
        Prefecture.values().filter { preferenceRepository.getCurrentPrectureCode() == it.prefCode }
            .last().prefecturePageLink()

    fun getCurrentCallLink() =
        Prefecture.values().filter { preferenceRepository.getCurrentPrectureCode() == it.prefCode }
            .last().prefectureCallLink()
}