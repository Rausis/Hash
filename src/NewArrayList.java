public class NewArrayList <T> {
    public Object[] data;
    public int size;
    public int index;

    public NewArrayList() {
        size = 0;
        data = new Object[10];
    }

    public void createArray(int capacity) {
        if (capacity <= 0){
            capacity = 10;
        }
        data = new Object[capacity];
        size = 0;
    }

    public void createArray() {
        data = new Object[10];
        size = 0;
    }

    public int getSize() {
        return size ;
    }

    public int getCapacity() {
        return data.length;
    }

    public void add(T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (size + 1 > data.length) {
            int newCapacity = data.length * 2;
            if (newCapacity < size + 1) {
                newCapacity = size + 1;
            }
            Object[] newData = new Object[newCapacity];
            int k = 0;
            while (k < size) {
                newData[k] = data[k];
                k++;
            }
            data = newData;
        }
        data[size] = element;
        size++;
    }

    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (size + 1 > data.length) {
            int newCapacity = data.length * 2;
            if (newCapacity < size + 1) {
                newCapacity = size + 1;
            }
            Object[] newData = new Object[newCapacity];
            int k = 0;
            while (k < size) {
                newData[k] = data[k];
                k++;
            }
            data = newData;
        }
        int i = size - 1;
        while (i >= index) {
            data[i + 1] = data[i];
            i--;
        }
        data[index] = element;
        size++;
    }

    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null;
        size--;
    }

    public void remove(T element) {
        int index = getElementIndex(element);
        if (index >= 0) {
            remove(index);
        }
    }

    public void setElement(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        data[index] = element;
    }

    public T getElement(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) data[index];
    }

    public int getElementIndex(T element) {
        int elementIndex = 0;
        int J = 0;
        while (J < size) {
            if (element == null) {
                if (data[elementIndex] == null) {
                    return elementIndex;
                }
            } else {
                if (element.equals(data[elementIndex])) {
                    return elementIndex;
                }
            }
            elementIndex++;
            J++;
        }
        return -1;
    }

    public boolean contains(T element) {
        int index = getElementIndex(element);
        return index >= 0;
    }

    public String toString() {
        String s = "[";
        for (int i = 0; i < size; i++) {
            s += data[i];
            if (i < size - 1) s += ", ";
        }
        s += "]";
        return s;
    }
}
