package Assignment_3;
import java.util.*;

class Person {
    private String name;
    private String gender;
    private Person mother;
    private Person father;
    private List<Person> children;
    
    public Person(String name, String gender) {
        this.name = name;
        this.gender = gender;
        this.children = new ArrayList<>();
    }
    
    public String getName() { return name; }
    public String getGender() { return gender; }
    public Person getMother() { return mother; }
    public Person getFather() { return father; }
    public List<Person> getChildren() { return children; }
    
    public void setMother(Person mother) { this.mother = mother; }
    public void setFather(Person father) { this.father = father; }
    public void addChild(Person child) { children.add(child); }
}

class FamilyTree {
    private Map<String, Person> people;
    
    public FamilyTree() {
        this.people = new HashMap<>();
    }
    
    public void addPerson(String name, String gender) {
        people.put(name, new Person(name, gender));
    }
    
    // Establish parent-child relationship
    public void addParentChild(String parentName, String childName) {
        Person parent = people.get(parentName);
        Person child = people.get(childName);
        
        if (parent != null && child != null) {
            parent.addChild(child);
            if (parent.getGender().equals("male")) {
                child.setFather(parent);
            } else {
                child.setMother(parent);
            }
        }
    }
    
    public List<Person> getChildren(String name) {
        Person person = people.get(name);
        return person != null ? person.getChildren() : new ArrayList<>();
    }
    
    public Person getFather(String name) {
        Person person = people.get(name);
        return person != null ? person.getFather() : null;
    }
    
    public Person getMother(String name) {
        Person person = people.get(name);
        return person != null ? person.getMother() : null;
    }
    
    public List<Person> getSiblings(String name) {
        Person person = people.get(name);
        if (person == null) return new ArrayList<>();
        
        List<Person> siblings = new ArrayList<>();
        Person father = person.getFather();
        Person mother = person.getMother();
        
        if (father != null) {
            for (Person child : father.getChildren()) {
                if (!child.getName().equals(name)) {
                    siblings.add(child);
                }
            }
        }
        
        return siblings;
    }
    
    // Display family tree
    public void displayFamilyTree() {
        for (Person person : people.values()) {
            System.out.println("\n" + person.getName() + " (" + person.getGender() + ")");
            
            if (person.getFather() != null) {
                System.out.println("  Father: " + person.getFather().getName());
            }
            
            if (person.getMother() != null) {
                System.out.println("  Mother: " + person.getMother().getName());
            }
            
            if (!person.getChildren().isEmpty()) {
                System.out.println("  Children: " + 
                    person.getChildren().stream()
                         .map(Person::getName)
                         .collect(java.util.stream.Collectors.joining(", ")));
            }
        }
    }
}

public class FamilyTreeParser {
    public static void main(String[] args) {
        FamilyTree familyTree = new FamilyTree();
        
        familyTree.addPerson("John", "male");
        familyTree.addPerson("Mary", "female");
        familyTree.addPerson("Tom", "male");
        familyTree.addPerson("Lisa", "female");
        familyTree.addPerson("Baby", "male");
        
        familyTree.addParentChild("John", "Tom");
        familyTree.addParentChild("Mary", "Tom");
        familyTree.addParentChild("John", "Lisa");
        familyTree.addParentChild("Mary", "Lisa");
        familyTree.addParentChild("Tom", "Baby");
        
        System.out.println("--- COMPLETE FAMILY TREE ---");
        familyTree.displayFamilyTree();
        
        // Query examples
        System.out.println("\n--- QUERY RESULTS ---");
        System.out.println("Children of John: " + 
            familyTree.getChildren("John").stream()
                     .map(Person::getName)
                     .collect(java.util.stream.Collectors.joining(", ")));
        
        System.out.println("Father of Tom: " + 
            (familyTree.getFather("Tom") != null ? familyTree.getFather("Tom").getName() : "Unknown"));
        
        System.out.println("Siblings of Lisa: " + 
            familyTree.getSiblings("Lisa").stream()
                     .map(Person::getName)
                     .collect(java.util.stream.Collectors.joining(", ")));
    }
}