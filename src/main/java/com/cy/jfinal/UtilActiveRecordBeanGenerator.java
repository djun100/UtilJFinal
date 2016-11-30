package com.cy.jfinal;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.c3p0.C3p0Plugin;

import javax.sql.DataSource;

/**
 * 在数据库表有任何变动时，运行一下 main 方法，极速响应变化进行代码重构，指定为idea目录结构
 */
public class UtilActiveRecordBeanGenerator {

//	public static void main(String[] args) {
//		generateActiveRecordBean("com.demo.common.model","a_little_config.txt");
//	}

	/**
	 * @param packageModel	eg:   com.demo.common.model
	 * @param dataSourcePathName 	"a_little_config.txt"	in resources folder
	 */
	public static void generateActiveRecordBean(String packageModel,String dataSourcePathName){
		// model 所使用的包名 (MappingKit 默认使用的包名)
		String modelPackageName = packageModel;
		// base model 所使用的包名
		String baseModelPackageName = modelPackageName+".base";
		// base model 文件保存路径
		String baseModelOutputDir = PathKit.getWebRootPath() + "/../src/main/java/"+baseModelPackageName.replace(".","/");

		// model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
		String modelOutputDir = baseModelOutputDir + "/..";

		// 创建生成器
		Generator gernerator = new Generator(getDataSource(dataSourcePathName), baseModelPackageName, baseModelOutputDir, modelPackageName,
				modelOutputDir);
		// 添加不需要生成的表名
		gernerator.addExcludedTable("adv");
		// 设置是否在 Model 中生成 dao 对象
		gernerator.setGenerateDaoInModel(true);
		// 设置是否生成字典文件
		gernerator.setGenerateDataDictionary(false);
		// 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
		gernerator.setRemovedTableNamePrefixes("t_");
		// 生成
		gernerator.generate();
	}

	/**
	 * @param dataSourcePathName	"a_little_config.txt"
	 * @return
	 */
	private static DataSource getDataSource(String dataSourcePathName) {
		PropKit.use(dataSourcePathName);
		C3p0Plugin c3p0Plugin = createC3p0Plugin();
		c3p0Plugin.start();
		return c3p0Plugin.getDataSource();
	}

	private static C3p0Plugin createC3p0Plugin() {
		return new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
	}
}




