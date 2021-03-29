package com.xianpeng.govass.fragment.mine

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.tencent.mmkv.MMKV
import com.xianpeng.govass.Constants
import com.xianpeng.govass.Constants.Companion.NORMAL_MSG_PAGE
import com.xianpeng.govass.R
import com.xianpeng.govass.activity.common.CommonListActivity
import com.xianpeng.govass.activity.login.LoginActivity
import com.xianpeng.govass.activity.updatepwd.UpdatePwdActivity
import com.xianpeng.govass.activity.userinfo.UserInfoActivity
import com.xianpeng.govass.base.BaseFragment
import com.xianpeng.govass.bean.BaseResponse
import com.xianpeng.govass.ext.showMessage
import com.xianpeng.govass.ext.toastError
import com.xianpeng.govass.util.CacheUtil
import com.xuexiang.xui.widget.actionbar.TitleBar
import com.xuexiang.xui.widget.textview.badge.BadgeView
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.titlebar_layout.*
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class MineFragment : BaseFragment<BaseViewModel>() {
    override fun layoutId(): Int = R.layout.fragment_mine

    override fun initView(savedInstanceState: Bundle?) {
        getUnReadMsgCountAndShow()
        titlebar.setTitle("个人中心")
        titlebar.setLeftVisible(false)
        titlebar.addAction(object : TitleBar.Action {
            override fun leftPadding(): Int = 0
            override fun performAction(view: View?) {
                startActivity(
                    Intent(activity, CommonListActivity::class.java).putExtra(
                        "pageParam",
                        NORMAL_MSG_PAGE
                    )
                )
            }

            override fun rightPadding(): Int = 0
            override fun getText(): String = ""
            override fun getDrawable(): Int = R.drawable.ic_baseline_message_24
        })
        account.setLeftString(CacheUtil.getUser()?.realname)

        account.setOnClickListener { startActivity(Intent(activity, UserInfoActivity::class.java)) }
        tuiguang.setOnClickListener { }
        kefu.setOnClickListener { }
        switchAccount.setOnClickListener {
            showMessage("确定切换账号登录吗，确定后将清除数据！", positiveAction = {
                CacheUtil.clearUserInfo()
                val switchAccount = Intent(activity, LoginActivity::class.java)
                startActivity(switchAccount)
                activity?.finish()
            }, negativeButtonText = "取消")
        }
        updatePwd.setOnClickListener {
            startActivity(Intent(activity, UpdatePwdActivity::class.java))
        }
        logout.setOnClickListener {
            showMessage("确定退出当前登陆账号吗，确定后将清除数据！", positiveAction = {
                CacheUtil.clearUserInfo()
                activity?.finish()
                System.exit(0)
            }, negativeButtonText = "取消")
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            getUnReadMsgCountAndShow()
        }
    }

    private fun getUnReadMsgCountAndShow() {
        AndroidNetworking.get(Constants.GET_UNREADMSG_COUNT)
            .addHeaders("token", MMKV.defaultMMKV().getString("loginToken", ""))
            .build()
            .getAsObject(BaseResponse::class.java, object :
                ParsedRequestListener<BaseResponse> {
                override fun onResponse(response: BaseResponse?) {
                    if (response == null) {
                        toastError("获取未读消息数失败，请稍后再试")
                        return
                    }
                    if (response.code != 0) {
                        toastError(response.msg)
                        return
                    }
                    BadgeView(context).bindTarget(titlebar).badgeNumber = response.data!!.toInt()
                }

                override fun onError(anError: ANError?) {
                    toastError("获取未读消息数失败，请稍后再试，msg=" + anError!!.errorDetail)
                }
            })
    }
}

