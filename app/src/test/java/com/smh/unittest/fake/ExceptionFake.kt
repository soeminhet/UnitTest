package com.smh.unittest.fake

import com.smh.unittest.network.DataException
import com.smh.unittest.network.ERROR_MESSAGE_GENERAL
import com.smh.unittest.network.ERROR_TITLE_GENERAL

val ApiException = DataException.Api(message = ERROR_MESSAGE_GENERAL, errorCode = 404, title = ERROR_TITLE_GENERAL)