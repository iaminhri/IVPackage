package IVPackage;

/**     importing libraries.     */

import BasicIO.*;
import com.sun.javafx.scene.NodeEventDispatcher;

/**     Main Class     */
public class IVEditor
{
    /**     Instanced Variables     */

    int button = 0;
    private final int fixedLength = 25;
    private BasicForm form = null;
    private String RadioLabel [] = {"Insert", "Delete", "Replace"};
    private Node document [] = new Node[fixedLength];
    public Node currentHead, list;

    int line, start, end;
    String text;

    /**     Constructors     */
    public IVEditor()
    {
        BuildForm();
        formActions(form);
        form.close();
    }

    //Selects from radio button and does insert or deletion or replace based on radio button selection.
    private void formActions(BasicForm f)
    {
        while(true)
        {
            button = form.accept();
            /**     Selected radio Buttons can apply edit.
             *      @ doInsertInArray() -> does insert operation on various input
             *      @ printDocument() -> prints the updated array for linked list.
             *      @ deletionProcess() -> deletes from the input.
             *      @ Replace() -> replaces within certain range and replaces with newly converted text to linked list.
             * */
            if( button == 0 ) {
                if (form.readString("Action") == RadioLabel[0]) {
                    doInsertInArray();
                    printDocument();
                }
                else if (form.readString("Action") == RadioLabel[1]) {
                    deletionProcess();
                    printDocument();
                }
                else if (form.readString("Action") == RadioLabel[2]) {
                    replace();
                    printDocument();
                }
            }
            else
                break;
        }
    }

    //Finds the length of the Current Head
    private int findLength()
    {
        Node p = currentHead;
        int c = 0;
        while(p != null) {
            c += 1;
            p = p.next;
        }

        return c;
    }

    // prints the document out of the Linked list contained array.
    private void printDocument()
    {
        form.clear("out");
        Node temp;

        for(int i = 0; i < document.length; i++)
        {
            form.writeString("out", i + " : ");

            temp = document[i];

            while(temp != null)
            {
                form.writeC("out", temp.item);
                temp = temp.next;
            }
            form.writeString("out", "\n");
        }
    }
    /**     After inserting at ith position shifts all the elements on cell down     */
    private void shiftDown(int line)
    {
        for(int i = document.length-1; i >= line; i--)
        {
            if(document[i] != null)
            {
                if( (i+1) < document.length)
                {
                    document[i+1] = document[i];
                }
            }
        }
    }

    /**     Does Basic insertion     */
    private void insertion(char ch)
    {
        Node p = currentHead;

        if(p == null)
            currentHead = new Node(ch, null);
        else{
            while(p.next != null) {
                p = p.next;
            }
            p.next = new Node(ch, null);
        }
    }

    /**     Inserts at anywhere
     *      @Param ListHead -> contains last node of the
     *      temporary text that is converted into linked list and appends them.
     *      @Param pointAt -> the position in the line number for it to insert
     *      @Param lineNumber -> contains the lineNumber given by the user.
     */
    private void insertionAtAnywhere(Node listHead, int pointAt, int lineNumber)
    {
        Node lastNode = listHead;
        currentHead = document[lineNumber];
        Node p = currentHead;
        int i = 1;
        Node temp;

        if( pointAt == 0 ){
            p = this.list;
            lastNode.next = currentHead;
            document[lineNumber] = p;
            currentHead = document[lineNumber];
        }
        else if(pointAt == findLength())
        {
            while(p.next != null) {
                p = p.next;
            }
            p.next = this.list;
        }
        else if(pointAt > findLength())
            return;
        else
        {
            while(i < pointAt) {
                p = p.next;
                i++;
            }
            temp = p.next;
            p.next = this.list;
            lastNode.next = temp;
        }
    }

    /**
     *      Helps convert the text from input to convert into linked list
     *      and appends them together, checks the edge cases with insertion and
     *      calls the shiftDown() -> shifts everything one node below.
     */
    private void doInsertInArray()
    {
        Node SendLastNode = null;
        int lineNumber = form.readInt("line");
        int startNumber = form.readInt("start");

        String text;
        text = form.readString("text");

        char index;

        //convert the input data into linked list and append them.
        index = text.charAt(0);

        if(lineNumber >= document.length)
            return;

        if(startNumber != -1)
        {
            this.list = new Node(text.charAt(0), null);
            Node p = this.list;

            for(int i = 1; i < text.length(); i++) {
                p.next = new Node(text.charAt(i), null);
                p = p.next;
            }
            SendLastNode = p;
        }

        if( (document[lineNumber] == null && startNumber == -1) || (document[lineNumber] != null && startNumber == -1)) {
            if(document[lineNumber] != null) {
                shiftDown(lineNumber);
            }
            document[lineNumber] = new Node(index, null);
            this.currentHead = document[lineNumber];
            for(int i = 1; i < text.length(); i++)
            {
                if(startNumber == -1)
                    insertion(text.charAt(i));
            }
        }

        else {
                insertionAtAnywhere(SendLastNode, startNumber, lineNumber);
        }
    }

    /**     Shifts Up     */
    public void shiftUp(int line)
    {
        int c = 0;
        int stop;

        for(int i = document.length-1; i >= 0; i--)
        {
            if(document[i] != null)
                c++;
        }
        stop = document.length - 1 - c;
        for(int i = line; i <= stop; i++)
        {
            if( i > 0) {
                document[i-1] = document[i];
            }
        }
    }
    /**     Deletes at ith position and given positions.     */
    public void deletionProcess()
    {
        line = form.readInt("line");
        start = form.readInt("start");
        end = form.readInt("end");

        currentHead = document[line];
        Node p = currentHead;
        Node q = null, prev = null;

        if(start == -1 && end == -1)
        {
            document[line] = null;
            shiftUp(line);
        }
        else if(start != -1 && end <= 0)
        {
            if(start == 0)
            {
                if(p == null)
                    return;
                else
                    document[line] = p.next;
            }
            else if(start >= findLength())
                return;
            else
            {
                int i = 1;
                if(p == null)
                    return;

                while(i <= start)
                {
                    q = p;
                    p = p.next;
                    i++;
                }
                if(p == null)
                    q.next = null;
                else
                    q.next = p.next;
            }
        }
        else if(start != -1 && end != -1)
        {
            if(start == 0 && end <= findLength())
            {
                int i = 0;
                while(i < end){
                    p = p.next;
                    i++;
                }
                document[line] = p.next;
            }
            else if (start <= findLength() && end <= findLength())
            {
                int i = 0;
                q = currentHead;

                while(true) {
                    if(p != null) {
                        if(i < start) {
                            prev = p;
                            p = p.next;
                        }
                        if(i <= end) {
                            q = q.next;
                        }
                        else
                            break;
                    }
                    i++;
                }
                prev.next = q.next;
            }
            else if(start <= findLength() && end >= findLength())
            {
                if(p == null)
                    return;
                int i = 0;
                while(i < start) {
                    p = p.next;
                    i++;
                }
                p.next = null;
            }
            else if(start >= findLength() && end <= findLength())
                return;
            else
                return;
        }
    }
    /**
     *      Replaces text (converted to linked list) within range
     *      and check edge cases for replace.
     */
    private void replace()
    {
        line = form.readInt("line");
        start = form.readInt("start");
        end = form.readInt("end");
        text = form.readString("text");

        currentHead = document[line];
        Node p = currentHead, list = null;

        this.list = new Node(text.charAt(0), null);
        Node temp = this.list;

        for(int i = 1; i < text.length(); i++) {
            temp.next = new Node(text.charAt(i), null);
            temp = temp.next;
        }

        Node q = currentHead;

        if(start == -1 && end == -1 || start > end)
            return;

        if(start == 0 && end == 0)
        {
            p = this.list;
            temp.next = currentHead.next;
            document[line] = p;
            currentHead = document[line];
        }
        else if(start <= findLength() && end <= findLength())
        {
            int i = 0;

            while (i < start-1) {
                p = p.next;
                i++;
            }

            int j = 0;

            while(j <= end) {
                q = q.next;
                j++;
            }
            p.next = this.list;
            temp.next = q;
        }
        else
            return;
    }
    /**     Builds the GUI form for the program.    */
    private void BuildForm()
    {
        form = new BasicForm("Apply Edit", "Exit");
        form.setTitle("IV Editor");
        form.addTextField("line", "Line", 4,11,10);
        form.addTextField("start", "Start", 4, 100, 10);
        form.addTextField("end", "End", 4, 200, 10);
        form.addTextField("text", "Text", 30);
        form.addRadioButtons("Action", "Edit Action",  false, RadioLabel);
        form.addTextArea("out", "Output", 20, 40);

        form.writeInt("end", -1);
        form.writeInt("start", -1);


    }
    public static void main(String args[])
    {
        IVEditor editor = new IVEditor();
    }
}
