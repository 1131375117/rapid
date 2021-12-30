package cn.huacloud.taxpreference.sync.spider.processor;

import cn.huacloud.taxpreference.services.producer.entity.vos.DocCodeVO;
import cn.hutool.core.util.ReUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文号处理器
 * @author wangkh
 */
public interface DocCodeProcessors {

    Pattern pattern01 = Pattern.compile("[1-2]\\d{3}[年,-,' ']\\d{1,2}[月,-,' ']\\d{1,2}[日,-,' ']");
    Pattern pattern02 = Pattern.compile("[〔,\\[]*[1-2]\\d{3}[〕,\\]]*");
    Pattern pattern03 = Pattern.compile("[1-2]\\d{3}");
    Pattern pattern04 = Pattern.compile("[、,第]*\\d+[号]");
    Pattern pattern05 = Pattern.compile("\\d+");

    /**
     * 文号处理器
     */
    Function<String, DocCodeVO> docCode = codeStr -> {
        // 分割预处理
        String[] separators = {"，", ","};
        String target = null;
        for (String separator : separators) {
            if (codeStr.contains(separator)) {
                String[] split = StringUtils.split(codeStr, separator);
                target = split[split.length - 1];
                break;
            }
        }
        if (target == null) {
            target = codeStr;
        }

        // 多文号相连
        {
            List<Group> rawNumGroups = getAllGroups(pattern04, target);
            int index;
            int num = 2;
            while ((index = rawNumGroups.size() - num) >= 0) {
                Group group1 = rawNumGroups.get(index);
                Group group2 = rawNumGroups.get(index + 1);
                // 只有两个文号距离超过1才进行截取
                if (group2.getStart() - group1.getEnd() > 1) {
                    target = target.substring(group1.getEnd());
                    break;
                }
                num ++;
            }
        }

        target = target.trim();

        target = ReUtil.delFirst(pattern01, target);

        List<Group> rawYearGroups = getAllGroups(pattern02, target);

        String wordCode;

        Integer yearCode = null;

        Integer numCode = null;

        String numTarget;

        if (rawYearGroups.isEmpty()) {
            // 没有年号
            List<Group> rawNumGroups = getAllGroups(pattern04, target);
            if (rawNumGroups.isEmpty()) {
                // TODO 中文数字转换
                wordCode = target;
            } else {
                Group rawNumGroup = rawNumGroups.get(rawNumGroups.size() - 1);
                wordCode = target.substring(0, rawNumGroup.getStart());
                numCode = getNum(rawNumGroup.getGroup());
            }
        } else {
            List<Group> yearCodes = getAllGroups(pattern03, target);
            Group rowYearGroup = rawYearGroups.get(rawYearGroups.size() - 1);
            if (rawYearGroups.size() == 1) {
                yearCode = Integer.parseInt(yearCodes.get(0).getGroup());
                wordCode = target.substring(0, rowYearGroup.getStart());
            } else {
                yearCode = Integer.parseInt(yearCodes.get(yearCodes.size() - 1).getGroup());
                wordCode = target.substring(rawYearGroups.get(rawYearGroups.size() - 2).getEnd(), rowYearGroup.getStart());
            }
            numTarget = target.substring(rowYearGroup.getEnd());
            // 获取文号
            numCode = getNum(numTarget);
        }

        DocCodeVO docCodeVO = new DocCodeVO();
        docCodeVO.setDocWordCode(wordCode);
        docCodeVO.setDocYearCode(yearCode);
        docCodeVO.setDocNumCode(numCode);
        return docCodeVO;
    };

    static Integer getNum(String numTarget) {
        List<Group> allGroups = getAllGroups(pattern04, numTarget);
        if (allGroups.isEmpty()) {
            // 中文数字转换
            return null;
        }
        String group = allGroups.get(allGroups.size() - 1).group;
        List<Group> groups = getAllGroups(pattern05, group);
        Group sub = groups.get(groups.size() - 1);
        return Integer.parseInt(sub.getGroup());
    }

    static List<Group> getAllGroups(Pattern pattern, CharSequence content) {
        Matcher matcher = pattern.matcher(content);
        List<Group> groups = new ArrayList<>();
        while (matcher.find()) {
            groups.add(new Group().setGroup(matcher.group())
                    .setStart(matcher.start())
                    .setEnd(matcher.end()));
        }
        return groups;
    }

    @Accessors(chain = true)
    @Data
    class Group {
        String group;
        int start;
        int end;
    }
}