package com.xianpeng.govass.bean

/**
 * 多文件上传
 */
class BaseFileUploadRes {
    var code: Int = -1
    var msg: String = ""
    var data: List<Attachment> = ArrayList()
}