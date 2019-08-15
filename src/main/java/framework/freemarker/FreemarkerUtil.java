package framework.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.StringWriter;
import java.util.Map;

public class FreemarkerUtil
{
    private static final Logger logger = LoggerFactory.getLogger(FreemarkerUtil.class);
    private static volatile FreemarkerUtil freemarkerUtil;
    private Configuration freemarkerConfig;

    public static FreemarkerUtil getInstance ()
    {
        if(freemarkerUtil == null)
        {
            synchronized (FreemarkerUtil.class)
            {
                if(freemarkerUtil == null)
                {
                    freemarkerUtil = new FreemarkerUtil();
                }
            }
        }
        return freemarkerUtil;
    }

    private FreemarkerUtil ()
    {
        initFreeMarker();
    }

    private void initFreeMarker ()
    {
        try
        {
            File appRoot = new File(FreemarkerUtil.class.getClassLoader().getResource("").getFile());
            freemarkerConfig = new Configuration(Configuration.VERSION_2_3_28);
            freemarkerConfig.setDirectoryForTemplateLoading(appRoot); // 设置模板根目录
            freemarkerConfig.setDefaultEncoding("UTF-8");
            freemarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            freemarkerConfig.setLogTemplateExceptions(true);
        } catch (Exception e)
        {
            logger.error("freemarker init failure", e);
        }
    }

    public String processTemplateIntoString (String templatePath, Map<String, Object> model)
    {
        Template template = null;
        StringWriter result = new StringWriter();
        try
        {
            template = freemarkerConfig.getTemplate(templatePath);
            template.process(model, result);
        } catch (Exception e)
        {
            logger.error("freemarker processTemplateIntoString failure", e);
        }
        return result.toString();
    }
}
