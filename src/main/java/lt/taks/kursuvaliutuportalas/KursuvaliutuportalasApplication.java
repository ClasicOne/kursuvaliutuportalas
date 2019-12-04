package lt.taks.kursuvaliutuportalas;

import lt.taks.kursuvaliutuportalas.repository.CurrencyTimelineRepo;
import lt.taks.kursuvaliutuportalas.repository.FxRatesRepo;
import lt.taks.kursuvaliutuportalas.repository.LastUpdateTimeRepo;
import lt.taks.kursuvaliutuportalas.services.RestService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KursuvaliutuportalasApplication {

    public static void main(String[] args) throws SchedulerException {
        SpringApplication.run(KursuvaliutuportalasApplication.class, args);

//        Scheduler();
    }

    public static void Scheduler(FxRatesRepo fxRatesRepo, CurrencyTimelineRepo currencyTimelineRepo, LastUpdateTimeRepo lastUpdateTimeRepo) throws SchedulerException {
        JobDetail jobBuilder = JobBuilder.newJob(SimpleJob.class)
                .withIdentity("dummyJobName")
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("TriggerName", "group1")
                .forJob(jobBuilder)
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 1 * * ?")) // https://www.freeformatter.com/cron-expression-generator-quartz.html#cronexpressionexamples
                .build();
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        try {
            scheduler.getContext().put("fx", fxRatesRepo);
            scheduler.getContext().put("time", currencyTimelineRepo);
            scheduler.getContext().put("date", lastUpdateTimeRepo);

            scheduler.start();
            scheduler.scheduleJob(jobBuilder, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
