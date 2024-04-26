package domain

class GameLogic(private val wordRepository: WordRepository, private val dataRepository: DataRepository, private val userRepository: UserRepository) {
    var users = mutableListOf<User>()

    fun initializeGame(numberofSpies:Int, numberofUsers:Int){
        val numberofPlayers = numberofUsers-numberofSpies
        dataRepository.setUser(userRepository.createUsers(numberofSpies, numberofPlayers))
        dataRepository.setWord(wordRepository.getRandomWord(wordRepository.loadWords()))
        this.users = dataRepository.getAllUsers().toMutableList()
    }

    fun displayOneRole() {
        if (users.isEmpty()) { return }

        val randomUser = userRepository.selctRandomUser(users)
        users.remove(randomUser)
        randomUser.displayRole()
        if (randomUser is Player) {
            printPlayerWord(randomUser)
        }
    }

    private fun printPlayerWord(player: Player) {
        println("Word: ${dataRepository.getWord()?.name}")
    }


    fun endGame(){
        userRepository.displayAllUserRoles(dataRepository.getAllUsers().toMutableList())
    }
}

package domain

interface WordRepository {
    fun loadWords():List<Word>
    fun createWord(wordId: Int, word: String): Word
    fun getRandomWord(words: List<Word>): Word
}

package applikation

import domain.Player
import domain.Spy
import domain.User
import domain.UserRepository


class ImpUserRepository : UserRepository{
    override fun createSpy(spyId: Int): Spy {
        return Spy(spyId,"Spy $spyId")
    }

    override fun createPlayer(playerId: Int): Player {
        return Player(playerId, "Player $playerId")
    }

    override fun createUsers(numberOfSpies: Int, numberOfPlayers: Int): List<User> {
        val users = mutableListOf<User>()
        for (i in 1..numberOfSpies) {
            users.add(createSpy(i))

        }
        for (i in 1..numberOfPlayers) {
            users.add(createPlayer(i))
        }
        return users
    }



    override fun displayAllUserRoles(userList: List<User>) {
        userList.forEach { user ->
            println("${user.username}: ")
            user.displayRole()
        }
    }

    override fun selctRandomUser(userList: MutableList<User>): User {
        val randomIndex = (0..<userList.size).random()
        return userList.removeAt(randomIndex)
    }
}