package com.demo.controller;

import com.crystaldecisions.sdk.occa.report.application.DataDefController;
import com.crystaldecisions.sdk.occa.report.application.ReportClientDocument;
import com.crystaldecisions.sdk.occa.report.data.Fields;
import com.crystaldecisions.sdk.occa.report.data.IParameterField;
import com.demo.utils.CRJavaHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

@RestController
@RequestMapping("/")
public class DemoController {

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public void test(HttpServletResponse response, Integer deptNoPar, String imagePath) throws Exception {
        //加载报表文件
        ReportClientDocument r = new ReportClientDocument();
        r.open("static/test2.rpt", 0);

        //更换数据源
        String username = "scott";
        String password = "123456";
        String driverName = "oracle.jdbc.OracleDriver";
        String connectionURL = "jdbc:oracle:thin:@localhost:1521:orcl";
        String jndiName = "JDBC(JNDI)";
        CRJavaHelper.changeDataSource(r, username, password, connectionURL, driverName, jndiName);

        //查询参数
        DataDefController dataDefController = r.getDataDefController();
        Fields<IParameterField> fields = dataDefController.getDataDefinition().getParameterFields();
        System.out.println("参数数量: " + fields.size());
        for (IParameterField field : fields) {
            System.out.println("参数： " + field);
        }

        //给参数赋值
        //第二个参数代表子报表，设置主报表参数，设置为空串
        CRJavaHelper.addDiscreteParameterValue(r,"","deptNoPar",deptNoPar);
        CRJavaHelper.addDiscreteParameterValue(r,"","imagePath",imagePath);

        CRJavaHelper.exportPDF(r,response,false);
    }
}
