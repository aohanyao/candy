package com.td.framework.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangxiugao on 2018/6/27
 */

public class EditTextInputFilter {


    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText EditText输入框
     */
    public static void setEditTextInputSpeChat(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                }
                if(dstart > 25 || dest.length() > 25){
                    return  "";
                }

                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？-]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止EditText输入特殊字符 和中文
     * @param editText EditText输入框
     */
    public static void setEditTextInput(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n") || source.equals(".")) {
                    return "";
                }

                String speChat = "[`~!@#$￥%^&*()+=|{}':;',\\[\\]<>/?~！@#、%……&*（）——+|{}【】‘；：”“’。，？-]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find()) {
                    return "";
                } else {
                    String regex = "^[\u4E00-\u9FA5]+$";
                    boolean isChinese = Pattern.matches(regex, source.toString());
                    if (isChinese) {
                        return "";
                    }
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }


}
