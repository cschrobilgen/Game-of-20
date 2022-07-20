package model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

// A model for the game of 20 questions. This type can be used to
// build a console based game of 20 questions or a GUI based game.
//
// @author Rick Mercer and YOUR NAME
//
public class GameTree {

  // BinaryTreeNode inner class used to create new nodes in the GameTree.
  private class TreeNode {

    // Instance variables
    private String data;
    private TreeNode left;
    private TreeNode right;

    TreeNode(String theData) {
      data = theData;
      left = null;
      right = null;
    }

    // This 2nd constructor is needed in a few methods, like privste build()
    TreeNode(String theData, TreeNode leftLink, TreeNode rightLink) {
      data = theData;
      left = leftLink;
      right = rightLink;
    }
  }

  // Instance variables
  private TreeNode root;
  private TreeNode currentNode;
  private Scanner scanner;
  private String currentFileName;
  private Scanner inputFile;
  private PrintWriter outFile;
 
  // Constructor needed to create the game. It should open the input 
  // file and call the recursive method build(). The String parameter
  // name is the name of the file from which we need to read the game 
  // questions and answers from.
  //
  public GameTree(String name) {
    currentFileName = name;
    try {
    	 inputFile = new Scanner(new File(currentFileName));
    	 root = build();
    	 currentNode = root;
    }
    catch (FileNotFoundException e) {
    }
  }
  
  private TreeNode build() {
	  String token = Scanner.nextLine().trim();
	  if (isAnswer(token)) {
		  return new TreeNode(token);
	  }
	  
	  TreeNode left = build();
	  TreeNode right = build();
	  return new TreeNode(token, left, right);
  }
  
  private boolean isAnswer(String token) {
	  return !token.endsWith("?");
  }

  // Add a new question and answer to the currentNode. If the current 
  // node is referencing the answer "parrot", 
  //     theGame.add("Does it swim?", "duck"); 
  // should change the GameTree like this:
  //
  // ......Feathers?......................Feathers? 
  // ....../......\......................./......\ 
  // ..parrot....horse.........Does it swim?.....horse
  // ............................./......\
  // ..........................duck.....parrot 
  //
  // @param newQuestion: The question to add where the old answer was.
  // @param newAnswer: The new yes answer to the new question.
  public void add(String newQuestion, String newAnswer) {
	  String oldAnswer = currentNode.data;
	  currentNode.data = newQuestion;
	  currentNode.left.data = newAnswer;
	  currentNode.right.data = oldAnswer;
  }

  // Return true if getCurrent() is an answer rather than a question. Return false
  // if the current node is an internal node rather than a leaf that is an answer.
  public boolean foundAnswer() {
    return isAnswer(currentNode.data);
  }

  // Return the data for the current node, which could be a
  // question or an answer.
  public String getCurrent() {
    return currentNode.data;
  }

  // Ask the game to update the current node in the tree by
  // going left for Choice.yes or right for Choice.no 
  // Example code:
  //   theGame.playerSelected(Choice.Yes);
  //
  public void playerSelected(Choice yesOrNo) {
    if (yesOrNo == Choice.YES) {
    	currentNode = currentNode.left;
    }
    else {
    	currentNode = currentNode.right;
    }
  }

  // Begin a game at the root of the tree. getCurrent should return the question
  // at the root of this GameTree.
  public void reStart() {
    currentNode = root;
  }

  // Overwrite the old file for this gameTree with the current state that may have
  // new questions added since the game started. Get all other method working first
  // Build this method last.
  public void saveGame() {
	  outFile = new PrintWriter(new FileOutputStream(currentFileName));
	  outFile = preOrderSave(root);
	  outFile.close();
  }
  
  private PrintWriter preOrderSave(TreeNode root) {
	  if (root == null) {
		  return;
	  }
	  
	  outFile.println(root.data);
	  preOrderSave(root.left);
	  preOrderSave(root.right);
  }

  // Method used to print out a text version of the game file.
  @Override
  public String toString() {
    accumulate = "";
    toString(root, 0);
    return accumulate;
  }

  // Used in both toString methods to add strings like "- - - "
  private String accumulate; 
  
  private void toString(TreeNode node, int lvl) {
    if (node != null) {
      toString(node.right, lvl + 1);
      for (int i = 1; i <= lvl; i++) {
        accumulate += "-  ";
      }
      accumulate = accumulate + node.data + " \n";
      toString(node.left, lvl + 1);
    }
  }
}
