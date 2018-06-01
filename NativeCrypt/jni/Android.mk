LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := libcryptography
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := cryptography.cpp

include $(BUILD_SHARED_LIBRARY)