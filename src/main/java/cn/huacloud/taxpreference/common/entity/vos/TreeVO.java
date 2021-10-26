package cn.huacloud.taxpreference.common.entity.vos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树形对象接口
 * @author wangkh
 */
public interface TreeVO<T> {

    /**
     * 主键ID
     * @return
     */
    Long getId();

    /**
     * 获取父主键ID, 根节点ID为 0 或者小于 0 或者为 null
     * @return
     */
    Long getPid();

    /**
     * 设置孩子节点
     */
    void setChildren(List<T> children);

    /**
     * 统一封装树形方法
     * @param treeList 未加工的树形对象集合
     * @return 加工后的树形对象集合
     */
    static <T extends TreeVO<T>> List<T> getTree(List<T> treeList) {
        Map<Long, List<T>> map = new HashMap<>();
        for (T vo : treeList) {
            List<T> list = map.computeIfAbsent(vo.getPid(), key -> new ArrayList<>());
            list.add(vo);
        }
        for (T vo : treeList) {
            vo.setChildren(map.get(vo.getId()));
        }
        return map.get(0L);
    }
}
