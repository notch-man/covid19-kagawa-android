package jp.covid19_kagawa.covid19information2.ui.guide

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.transition.Scene
import jp.covid19_kagawa.covid19information2.GuideSelection
import jp.covid19_kagawa.covid19information2.R
import jp.covid19_kagawa.covid19information2.TransitionData
import jp.covid19_kagawa.covid19information2.actioncreator.GuideActionCreator
import jp.covid19_kagawa.covid19information2.databinding.*
import jp.covid19_kagawa.covid19information2.observe
import jp.covid19_kagawa.covid19information2.store.GuideStore
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

private enum class SCENE {
    SCENE1,
    SCENE2,
    SCENE3,
    SCENE4
}

class GuideFragment : Fragment() {

    private lateinit var mView1: Scene1Binding
    private lateinit var mView2: Scene2Binding
    private lateinit var mView3: Scene3Binding
    private lateinit var mView4: Scene4Binding
    private lateinit var mScene1: Scene
    private lateinit var mScene2: Scene
    private lateinit var mScene3: Scene
    private lateinit var mScene4: Scene

    /** Transitions take place in this ViewGroup. We retain this for the dynamic transition on scene 4.  */
    private lateinit var mSceneRoot: ViewGroup
    private val store: GuideStore by viewModel()
    private val actionCreator: GuideActionCreator by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGuideBinding.inflate(inflater, container, false)
        val view: View = binding.root

        //元のビューの作成
        mSceneRoot = view.findViewById<View>(R.id.scene_root) as ViewGroup
        //各シーンのビューのインスタンスを作成
        mView1 = Scene1Binding.inflate(inflater, container, false)
        mView1.scene1GotoNext.setOnClickListener {
            //Scene2へ遷移させる
            actionCreator.changeGuideScene(selectScene(SCENE.SCENE2))
        }
        mView2 = Scene2Binding.inflate(inflater, container, false)
        mView2.scene2GotoScene3.setOnClickListener {
            //Scene3へ遷移させる
            actionCreator.changeGuideScene(selectScene(SCENE.SCENE3))
        }
        mView3 = Scene3Binding.inflate(inflater, container, false)
        mView3.scene3GotoScene4.setOnClickListener {
            //Scene4へ遷移させる
            actionCreator.changeGuideScene(selectScene(SCENE.SCENE4))
        }
        mView4 = Scene4Binding.inflate(inflater, container, false)

//        //シーンのリソース周りの設定
//        mView1.buttonScene1.setOnClickListener {
//            actionCreator.changeGuideScene(selectScene(SCENE.SCENE2))
//        }
//        mView2.buttonScene2.setOnClickListener {
//            actionCreator.changeGuideScene(selectScene(SCENE.SCENE1))
//        }

        //シーンの登録
        mScene1 = Scene(mSceneRoot, mView1.root)
        mScene2 = Scene(mSceneRoot, mView2.root)
        mScene3 = Scene(mSceneRoot, mView3.root)
        mScene4 = Scene(mSceneRoot, mView4.root)

        //データバインディングの設定
        //Todo:消せ
        setClickItems()
        binding.lifecycleOwner = viewLifecycleOwner
        binding.item = store
        // END_INCLUDE(custom_transition_manager)
        actionCreator.getWebsiteLinks()
        //デフォルトのシーンに移動
        actionCreator.changeGuideScene(selectScene(SCENE.SCENE1))
        return view
    }

    private fun selectScene(scene: SCENE): TransitionData =
        when (scene) {
            SCENE.SCENE1 -> TransitionData(this.mScene1, GuideSelection.TOP)
            SCENE.SCENE2 -> TransitionData(this.mScene2, GuideSelection.SCENE2)
            SCENE.SCENE3 -> TransitionData(this.mScene3, GuideSelection.SCENE3)
            SCENE.SCENE4 -> TransitionData(this.mScene4, GuideSelection.SCENE4)
        }

    private fun setClickItems() {
        store.webSiteLink.observe(this) { link ->
            if (link.isNotEmpty()) {
                mView1.scene1GotoTel.setOnClickListener {
                    val test = link
                    val intent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(link)
                    )
                    startActivity(intent)
                }
                mView2.scene2GotoCall.setOnClickListener {
                    val intent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(link)
                    )
                    startActivity(intent)
                }
                mView3.scene3GotoCall.setOnClickListener {
                    val intent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(link)
                    )
                    startActivity(intent)
                }
                mView4.scene4GotoWeb.setOnClickListener {
                    val intent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(link)
                    )
                    startActivity(intent)
                }
                mView4.scene4GotoCall.setOnClickListener {
                    val intent = Intent(
                        Intent.ACTION_VIEW, Uri.parse(link)
                    )
                    startActivity(intent)
                }
            } else {
                mView1.scene1GotoTel.setOnClickListener {
                    Toast.makeText(context, "準備中です", Toast.LENGTH_SHORT).show()
                }
                mView2.scene2GotoCall.setOnClickListener {
                    Toast.makeText(context, "準備中です", Toast.LENGTH_SHORT).show()

                }
                mView3.scene3GotoCall.setOnClickListener {
                    Toast.makeText(context, "準備中です", Toast.LENGTH_SHORT).show()

                }
                mView4.scene4GotoWeb.setOnClickListener {
                    Toast.makeText(context, "準備中です", Toast.LENGTH_SHORT).show()

                }
                mView4.scene4GotoCall.setOnClickListener {
                    Toast.makeText(context, "準備中です", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}
