package com.caoxin.utils;

import java.util.*;

public class SensitiveWordUtils {

    public static Map<String, Object> dictionaryMap = new HashMap<>();

    /**
     * 生成关键词字典库
     */
    public static void initMap() {
        List<String> words = new ArrayList<>();
        words.add("毒品");
        words.add("嫖娼");
        words.add("赌博");
        words.add("色情");

        // map初始长度words.size()，整个字典库的入口字数(小于words.size()，因为不同的词可能会有相同的首字)
        Map<String, Object> map = new HashMap<>(words.size());
        // 遍历过程中当前层次的数据
        Map<String, Object> curMap = null;
        Iterator<String> iterator = words.iterator();
        while (iterator.hasNext()) {
            String word = iterator.next();
            curMap = map; //curMap有值的话，map也有值
            int len = word.length();
            for (int i = 0; i < len; i++) {
                // 遍历每个词的字
                String key = String.valueOf(word.charAt(i));
                // 当前字在当前层是否存在, 不存在则新建, 当前层数据指向下一个节点, 继续判断是否存在数据
                Map<String, Object> wordMap = (Map<String, Object>) curMap.get(key);
                if (wordMap == null) {
                    // 每个节点存在两个数据: 下一个节点和isEnd(是否结束标志)
                    wordMap = new HashMap<>(2);
                    wordMap.put("isEnd", "0");
                    curMap.put(key, wordMap);
                }
                curMap = wordMap; // 当前的wordMap层作为下次遍历的curMap层
                // 如果当前字是词的最后一个字，则将isEnd标志置1
                if (i == len - 1) {
                    curMap.put("isEnd", "1");
                }
            }
        }
        dictionaryMap = map;
    }

    /**
     * 搜索文本中某个文字是否匹配关键词
     *
     * @param text
     * @param beginIndex
     * @return
     */
    private static int checkWord(String text, int beginIndex) {
        if (dictionaryMap == null) {
            throw new RuntimeException("字典不能为空");
        }
        boolean isEnd = false;
        int wordLength = 0;
        Map<String, Object> curMap = dictionaryMap;
        int len = text.length();
        // 从文本的第beginIndex开始匹配
        for (int i = beginIndex; i < len; i++) {
            String key = String.valueOf(text.charAt(i));
            // 获取当前key的下一个节点
            curMap = (Map<String, Object>) curMap.get(key);
            if (curMap == null) {
                break;
            } else {
                wordLength++;
                if ("1".equals(curMap.get("isEnd"))) {
                    isEnd = true;
                }
            }
        }
        if (!isEnd) {
            wordLength = 0;
        }
        return wordLength;
    }

    /**
     * 获取匹配的关键词和命中次数
     *
     * @param text
     * @return
     */
    public static String matchWords(String text) {
        String result = text;
        Map<String, Integer> wordMap = new HashMap<>();
        int len = text.length();
        for (int i = 0; i < len; i++) {
            int wordLength = checkWord(text, i);
            if (wordLength > 0) {
                String word = text.substring(i, i + wordLength);
                result = result.replaceAll(word, "**");

                i += wordLength - 1;
            }
        }
        return result;
    }
}

