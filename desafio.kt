import java.text.Normalizer.Form

enum class Nivel { BASICO, INTERMEDIARIO, AVANCADO }

class Aluno(val nome: String, val cod: String) {
    override fun toString(): String {
        return "${this.nome} (${this.cod})"
    }
}

data class ConteudoEducacional(val nome: String, val duracao: Int = 60) {

    val nivel: Nivel?

    private val BASICO: Boolean = this.duracao > 0 && this.duracao <= 60
    private val INTERMEDIARIO: Boolean = this.duracao > 60 && this.duracao < 90
    private val AVANCADO: Boolean = this.duracao > 90

    init {
        this.nivel = if (BASICO) Nivel.BASICO else if (INTERMEDIARIO) Nivel.INTERMEDIARIO else if (AVANCADO) Nivel.AVANCADO else null
    }

    override fun toString(): String {
        return "(${this.nome} - Nível: ${this.nivel})"
    }
}

data class Formacao(val nome: String, var conteudos: Set<ConteudoEducacional?>) {

    val nivel: Nivel?
    val inscritos = mutableSetOf<Aluno>()

    fun matricular(vararg alunos: Aluno?) {
        for(aluno in alunos)
            if(aluno != null)
                this.inscritos.add(aluno)
    }

    init {
        var soma = 0.0
        for(conteudo in conteudos) {
            if(conteudo == null) break
            soma += when(conteudo.nivel) {
                Nivel.BASICO -> 1
                Nivel.INTERMEDIARIO -> 2
                Nivel.AVANCADO -> 3
                else -> 0
            }
        }
        val media: Int = Math.round((soma / this.conteudos.size)).toInt()

        this.nivel = when(media) {
            1 -> Nivel.BASICO
            2 -> Nivel.INTERMEDIARIO
            3 -> Nivel.AVANCADO
            else -> null
        }
    }

    override fun toString(): String {
        return "\n\nFormação ${this.nome}\tNivel: ${this.nivel}\n" +
                "Conteúdos: ${this.conteudos}\n" +
                "Inscritos: ${this.inscritos}"
    }
}

fun main() {
    val alunos: Map<Int, Aluno> = mapOf(
        1 to Aluno("Gustavo", "1908990"),
        2 to Aluno("Fernando", "5263365"),
        3 to Aluno("Fillipe", "2303032"),
        4 to Aluno("Ana", "2659865"),
        5 to Aluno("Carol", "4578457"),
        6 to Aluno("Carolina", "0245785"),
        7 to Aluno("Jéssica", "2014787"))

    val conteudos: Map<Int, ConteudoEducacional> = mapOf(
        1 to ConteudoEducacional("PHP Backend", 45),
        2 to ConteudoEducacional("Java Stream", 93),
        3 to ConteudoEducacional("Kotlin Functions", 65),
        4 to ConteudoEducacional("Docker", 105),
        5 to ConteudoEducacional("Angular Routes", 54),
        6 to ConteudoEducacional(".NET C# Migrations", 45),
        7 to ConteudoEducacional("React Native Introdução", 20),
        8 to ConteudoEducacional("Typescript Principais Padrões de Projetos (implementação)", 45))

    val formacoes: Map<Int, Formacao> = mapOf<Int, Formacao>(
        1 to Formacao("Fullstack: Kotlin + Angular", setOf(conteudos[3], conteudos[5], conteudos[4])),
        2 to Formacao(".NET + React Native", setOf(conteudos[6], conteudos[7])),
        3 to Formacao("Misturão", conteudos.values.toSet()))

    formacoes[1]?.matricular(* alunos.values.toTypedArray())
    formacoes[2]?.matricular(* arrayOf(alunos[1], alunos[5], alunos[7], alunos[3]))
    formacoes[3]?.matricular(* arrayOf(alunos[1], alunos[2], alunos[4], alunos[5], alunos[6]))

    for(formacao in formacoes)
        println(formacao.value)
}