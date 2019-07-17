package com.wwj.common.utils;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Created by Nancy on 2018/6/1.
 */
public class MabatisPlusGenerator {

    //生成文件所在项目路径
    private static String baseProjectPath = "D:";

    //基本包名
    private static String basePackage="com.tj.tjshop.sdk";
    //作者
    private static String authorName="Nancy";
    //要生成的表名
//    private static String[] tables= {"p_user"};
    //table前缀
    private static String prefix="tjs_";

    //数据库配置四要素
    private static String driverName = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://localhost:3306/biu?serverTimezone=GMT&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true";
    private static String username = "root";
    private static String password = "root123456";


    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    @SuppressWarnings("resource")
	public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    /**
     * 此入口根据数据库生成实体类 controller service mapper mapper.xml
     * @param args
     */
    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
//        String projectPath = "D:\\";
        gc.setOutputDir(baseProjectPath + "/src/main/java");
        gc.setAuthor(authorName);
        gc.setOpen(false);
        gc.setFileOverride(true);
        gc.setSwagger2(true); //实体属性 Swagger2 注解
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(url);
        // dsc.setSchemaName("public");
        dsc.setDriverName(driverName);
        dsc.setUsername(username);
        dsc.setPassword(password);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(scanner("模块名"));
        pc.setParent(basePackage);
        mpg.setPackageInfo(pc);

        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是 freemarker
        String voTplPath = "/templates/vo.java.ftl";
        String entityTplPath = "/templates/entity.java.ftl";
        String mapperXmlTplPath = "/templates/mapper.xml.ftl";
        // 如果模板引擎是 velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(entityTplPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 
                return baseProjectPath + "/src/main/java/"+StringUtils.replace(basePackage, ".", "/")+"/"+pc.getModuleName()+"/entity/"
                		+ tableInfo.getEntityName()  + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig(voTplPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 VO 的名称会跟着发生变化！！
                return baseProjectPath + "/src/main/java/"+StringUtils.replace(basePackage, ".", "/")+"/"+pc.getModuleName()+"/entity/vo/"
                		+ tableInfo.getEntityName() + "Vo" + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig(mapperXmlTplPath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 MapperXML 的名称会跟着发生变化！！
                return baseProjectPath + "/src/main/java/"+StringUtils.replace(basePackage, ".", "/")+"/"+pc.getModuleName()+"/mapper/"
                		+ tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();

        // 配置自定义输出模板
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        // templateConfig.setEntity("templates/entity2.java");
        // templateConfig.setService();
        // templateConfig.setController();

        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setLogicDeleteFieldName("del_flag");//表中逻辑删除字段
        strategy.setEntityColumnConstant(true);//【实体】是否生成字段常量（默认 false）
        strategy.setEntityBuilderModel(true);//【实体】是否为构建者模型（默认 false）
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setSuperControllerClass("com.tj.practice.common.BaseController");
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
//        strategy.setSuperEntityColumns("id");
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(prefix);
        mpg.setStrategy(strategy);
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }
}
