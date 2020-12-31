package demo.annotation;


@CourseInfoAnnotation(courseName = "lalala",courseTag = "面试",courseProfile = "balabala")
public class ImoocCourse {
    @PersonInfoAnnotation(name = "Alex",language = {"java","C++"})
    private String author;
    @CourseInfoAnnotation(courseName = "hahahaha",courseTag = "实战",courseProfile = "balabala",courseIndex = 144)
    public void getCourseInfo(){

    }
}
