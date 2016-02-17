package com.migeran.jgit.rebaser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RebaseCommand;
import org.eclipse.jgit.api.RebaseCommand.InteractiveHandler;
import org.eclipse.jgit.api.RebaseCommand.Operation;
import org.eclipse.jgit.api.RebaseResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.errors.IllegalTodoFileModification;
import org.eclipse.jgit.lib.RebaseTodoLine;
import org.eclipse.jgit.lib.RebaseTodoLine.Action;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;

public class Main {
		
	public static void main(String[] args) throws IOException, NoHeadException, WrongRepositoryStateException, GitAPIException {
		if (args.length < 2) {
			System.out.println("Usage: java -jar jgit-rebaser.jar <repo worktree dir> <upstream branch> [continue]");
			System.exit(1);
			return;
		}
		String repoDir = args[0];
		String branch = args[1];
		boolean toContinue = (args.length >= 3 && "continue".equalsIgnoreCase(args[2]));
		Repository repo = new RepositoryBuilder()
				.setWorkTree(new File(repoDir))
				.setup()
				.build();
		System.out.println(repo);
		try (Git git = new Git(repo)) {
            InteractiveHandler handler = new InteractiveHandler() {
                @Override
                public void prepareSteps(List<RebaseTodoLine> steps) {
                    for(RebaseTodoLine step : steps) {
                        try {
                            step.setAction(Action.PICK);
                        } catch (IllegalTodoFileModification e) {
                            throw new IllegalStateException(e);
                        }
                    }
                }

                @Override
                public String modifyCommitMessage(String oldMessage) {
                    return oldMessage;
                }
            };

            RebaseCommand cmd = git.rebase().setPreserveMerges(true).setUpstream(branch).runInteractively(handler);
            if (toContinue) {
            	cmd.setOperation(Operation.CONTINUE);
            }
            RebaseResult r = cmd.call();
            System.out.println("RebaseResult: " + r.getStatus());
        }		
		
	}
}