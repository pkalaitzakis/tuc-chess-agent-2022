This is a game entirely created by our AI Professor Mr. Georgios Chalkiadakis and his research team.
TUC-Chess is a point-oriented, alternate version of chess. It's played in a 7 by 5 grid and requires two players, each having
the following set of pieces: 7x pawns worth 1 point each, 2x rooks worth 3 points each and 1x king worth one point. 

- Pawns can only move forwards one step at a time if the target block is empty, or crosswise forwards if the target block is occupied by an
opponent's piece.

- Rooks can move forwards, backwards, left or right by three or less steps, only if an opposing or allied piece is not blocking its way.

- Kings move like rooks, but with the limitation of being able to take only one step at a time.

Additionally a prize might spawn after each player's turn, which if captured, there's a 95% chance that the captor is awarded with 1 point.

Each agent strives to be the player with the most points by the end of the match.
A match is considered to have ended if one of the following applies:

- A king was captured -> Player with most points wins the match (not necessarily the one who captured the king)
- All other pieces except the two kings were captured -> Player with most points wins the match
- 15 minutes have elapsed since the start of the match -> Player with most points wins the match

The following sketch represents the game board during the beginning of the game.

| BP  |  BR | BK | BR | BP |
| --- | --- | --- | --- | --- |
| BP | BP | BP | BP | BP |
| -- | -- | -- | -- | -- |
| P  | P  | P  | P  | P  | 
| -- | -- | -- | -- | -- |
| WP | WP | WP | WP | WP |
| WP | WR | WK | WR | WP |

In this project we created an agent that utilizes the min-max algorithm, along with move sorting and a-b pruning.
Essentially our agent is able to look 8 turns ahead, considering only the worthy-to-explore states in between, thus
saving time.

A TUC-Chess championship was held by the professor, in order to determine the best implementation of the required agent.
Out of of all the entries this particular agent managed to get a silver medal during the championship, suffering a close call loss 
during the finals (best-of-three).
