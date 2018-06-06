// IPersonManagerInterface.aidl
package com.example.songzeceng.studyofipc;

import com.example.songzeceng.studyofipc.Person;
// 即便是在同一包下，也要手动导入Person类

// Declare any non-default types here with import statements

interface IPersonManagerInterface {
    List<Person> getPeople();
    void addPerson(in Person person);
    Person updatePerson(out Person person);
    Person updatePerson2(inout Person person);
}
