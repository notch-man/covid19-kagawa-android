package jp.covid19_kagawa.covid19information.repository

import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import jp.covid19_kagawa.covid19information.Prefecture
import jp.covid19_kagawa.covid19information.data.mapper.TokyoMapper
import jp.covid19_kagawa.covid19information.data.repository.TokyoRepository
import jp.covid19_kagawa.covid19information.entity.InspectionData


class ChartRepository(
    private val tokyoRepository: TokyoRepository
) {
    fun fetchInspectData(prefecture: Prefecture): Single<List<InspectionData>> {
        return Single.create<List<InspectionData>> { emitter ->
            when (prefecture) {
                Prefecture.TOKYO -> {
                    tokyoRepository.fetchInspectData()
                        .subscribeOn(Schedulers.io())
                        .subscribeBy(
                            onSuccess = {
                                emitter.onSuccess(
                                    TokyoMapper.getInspectionData(
                                        it
                                    )
                                )
                            },
                            onError = { emitter.onError(it) }
                        )
                }
                else -> {
                    emitter.onError(Throwable(message = "Type Error!"))
                }
            }
        }
    }
}