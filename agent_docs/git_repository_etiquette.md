## Git Workflow
1. **Branch Naming**: `feature/description`, `fix/description`, `chore/description`
2. **Commit Messages**: Follow conventional commits
    - `feat:` New feature
    - `fix:` Bug fix
    - `docs:` Documentation only
    - `style:` Code style changes
    - `refactor:` Code refactoring
    - `test:` Test changes
    - `chore:` Build process or auxiliary tool changes

## Pull Request Process
1. **Self-Review**: Review your own PR first
2. **Description**: Use PR template, link to issue
3. **Tests**: All tests must pass
4. **Screenshots**: Include for UI changes
5. **Size**: Keep PRs under 400 lines when possible

## Code Review Guidelines
- Be constructive and specific
- Suggest improvements, don't just criticize
- Use "we" instead of "you" in comments
- Approve with "LGTM" (Looks Good To Me)

## Merge Strategy
- **Squash and merge** for feature branches
- **Rebase** for updating feature branches
- **No merge commits** in main branch
