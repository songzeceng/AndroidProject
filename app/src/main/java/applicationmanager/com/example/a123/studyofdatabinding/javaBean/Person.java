package applicationmanager.com.example.a123.studyofdatabinding.javaBean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import applicationmanager.com.example.a123.studyofdatabinding.BR;

public class Person  extends BaseObservable {
    private String name;
    private int age;
    private String url;

    public Person(String name, int age, String url) {
        this.name = name;
        this.age = age;
        this.url = url;
    }

    @Bindable
    // 在需要观察的属性对应的get方法上加Bindable注解
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
        // BR相当于R，name是其属性对应的id。调用此方法进行实时显示
    }

    @Bindable
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }

    @Bindable
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        notifyPropertyChanged(BR.url);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Person{");
        sb.append("name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append(", url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
