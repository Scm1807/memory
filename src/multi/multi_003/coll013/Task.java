package multi.multi_003.coll013;

public class Task implements Comparable<Task> {

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Task task) {
        return Integer.compare(this.id, task.id);
    }

    public String toString() {
        return this.id + "," + this.name;
    }

}
