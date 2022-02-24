package cn.huacloud.taxpreference.tool;

import cn.huacloud.taxpreference.BaseApplicationTest;
import cn.huacloud.taxpreference.common.enums.DocType;
import cn.huacloud.taxpreference.services.producer.entity.dos.PoliciesExplainDO;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesExplainMapper;
import cn.huacloud.taxpreference.services.producer.mapper.PoliciesMapper;
import cn.huacloud.taxpreference.services.sync.entity.dos.SpiderDataSyncDO;
import cn.huacloud.taxpreference.services.sync.mapper.SpiderDataSyncMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;

/**
 * @author fuhua
 **/
@Slf4j
public class HandlePolicyiesContentHtmlTool extends BaseApplicationTest {
    @Autowired
    SpiderDataSyncMapper spiderDataSyncMapper;
    @Autowired
    PoliciesMapper policiesMapper;
    @Autowired
    PoliciesExplainMapper policiesExplainMapper;

    public static final String DBDRIVER = "com.mysql.jdbc.Driver";
    //定义MySQL数据库的连接地址
    public static final String DBURL = "jdbc:mysql://172.16.18.28:3306/dataWEB?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    //MySQL数据库的连接用户名
    public static final String DBUSER = "tencentcloud";
    //MySQL数据库的连接密码
    public static final String DBPASS = "oOSI-5QArNyH_rW9b--Xxbfax4k0";

    @Test
    public void main() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        //政策法规 String sql = "select id , content, next_content from policy_data where next_content is not null";
        // 政策解读
        String sql = "select id , content, next_related_content from policy_data where next_related_content is not null";

        try {
            //加载驱动程序
            Class.forName(DBDRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            //连接MySQL数据库时，要写上连接的用户名和密码
            con = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString("id");
                System.out.print("id:" + id + " ");
                String content = rs.getString("content");
                // String next_content = rs.getString("next_content");
                String next_related_content = rs.getString("next_related_content");
                SpiderDataSyncDO spiderDataSyncDO = spiderDataSyncMapper.getSpiderDataSyncDO(DocType.POLICIES_EXPLAIN, id);
                if (spiderDataSyncDO != null) {
                    /*
                     * 修改政策法规
                     * */
             /*     PoliciesDO policiesDO = new PoliciesDO();
                    policiesDO.setContent(next_content);
                    policiesDO.setId(spiderDataSyncDO.getDocId());
                    policiesMapper.updateById(policiesDO);*/
                    /*

                     * 修改政策解读
                     *
                     * */
                    PoliciesExplainDO policiesExplainDO=new PoliciesExplainDO();
                    policiesExplainDO.setContent(next_related_content);
                    policiesExplainDO.setId(spiderDataSyncDO.getDocId());
                    policiesExplainMapper.updateById(policiesExplainDO);

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            //关闭数据库
            assert con != null;
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
