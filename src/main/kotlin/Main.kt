import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Класс для представления вопроса
data class Question(val questionText: String, val options: List<String>)

class TemperamentTestApp : JFrame("Тест на определение темперамента") {

    // Менеджер компоновки для смены панелей (экранов)
    private val cardLayout = CardLayout()
    private val mainPanel = JPanel(cardLayout)

    // Данные теста
    var fullName: String = ""
    val answers = mutableListOf<String>()
    var currentQuestionIndex = 0

    // Список вопросов
    val questions = listOf(
        Question(
            "Вопрос 1. Как вы реагируете на внезапные изменения планов?",
            listOf(
                "А) Быстро адаптируюсь и продолжаю двигаться дальше.",
                "Б) Сначала злюсь, но потом успокаиваюсь и начинаю искать новые пути решения.",
                "В) Спокойно воспринимаю изменения и продолжаю выполнять свои задачи.",
                "Г) Меня это сильно беспокоит, и я долго переживаю из-за изменений."
            )
        ),
        Question(
            "Вопрос 2. Как часто вы проявляете эмоции?",
            listOf(
                "А) Эмоции проявляются ярко и бурно, особенно радость и гнев.",
                "Б) Эмоциональный фон постоянно меняется, но в целом настроение позитивное.",
                "В) Эмоций мало, веду себя спокойно и уравновешенно.",
                "Г) Часто испытываю грусть или тревогу, эмоции глубокие, но скрытые."
            )
        ),
        Question(
            "Вопрос 3. Каково ваше отношение к новым знакомствам?",
            listOf(
                "А) Легко завожу новые знакомства, но могу быстро потерять интерес.",
                "Б) Обожаю общаться с новыми людьми, всегда открыт для новых знакомств.",
                "В) Новые люди вызывают у меня любопытство, но я предпочитаю наблюдать со стороны.",
                "Г) Новым людям сложно завоевать мое доверие, я осторожен в общении."
            )
        ),
        Question(
            "Вопрос 4. Какова ваша реакция на критику?",
            listOf(
                "А) Реагирую резко, могу вспылить, но быстро остываю.",
                "Б) Вначале расстроюсь, но постараюсь извлечь урок и улучшить себя.",
                "В) Критику воспринимаю спокойно, стараюсь проанализировать и сделать выводы.",
                "Г) Долго переживаю и чувствую себя подавленным после критики."
            )
        ),
        Question(
            "Вопрос 5. Какое ваше отношение ко времени?",
            listOf(
                "А) Всегда спешу, делаю много дел одновременно, иногда забываю о сроках.",
                "Б) Стараюсь успевать везде вовремя, но иногда могу немного опоздать.",
                "В) Всегда придерживаюсь расписания, редко опаздываю.",
                "Г) Иногда затягиваю выполнение задач, но стараюсь закончить всё вовремя."
            )
        ),
        Question(
            "Вопрос 6. Каковы ваши реакции на стресс?",
            listOf(
                "А) Стресс вызывает вспышки гнева и раздражения.",
                "Б) Стрессовая ситуация активизирует меня, стараюсь быстрее решить проблему.",
                "В) Сохраняю спокойствие и ищу рациональные способы решения проблемы.",
                "Г) Стресс приводит к унынию и чувству беспомощности."
            )
        ),
        Question(
            "Вопрос 7. Каков ваш подход к работе?",
            listOf(
                "А) Работоспособность высокая, но концентрация быстро падает.",
                "Б) Много энергии, работаю продуктивно, но люблю разнообразие.",
                "В) Медленно, но уверенно выполняю поставленные задачи.",
                "Г) Аккуратен и внимателен к деталям, но боюсь совершить ошибку."
            )
        ),
        Question(
            "Вопрос 8. Как вы принимаете решения?",
            listOf(
                "А) Решения принимаются быстро, иногда импульсивно.",
                "Б) Принимаю решения оперативно, опираясь на интуицию.",
                "В) Прежде чем принять решение, тщательно обдумываю все варианты.",
                "Г) Долгое размышление и сомнения перед принятием решения."
            )
        ),
        Question(
            "Вопрос 9. Какая у вас реакция на неудачи?",
            listOf(
                "А) Быстро разочаровываюсь, могу бросить начатое.",
                "Б) Немного расстраиваюсь, но вскоре возвращаюсь к делу с новой энергией.",
                "В) Спокойно принимаю неудачу как часть процесса, продолжаю работать над задачей.",
                "Г) Неудача надолго выбивает из колеи, тяжело восстанавливаться."
            )
        ),
        Question(
            "Вопрос 10. Каково ваше отношение к рутине?",
            listOf(
                "А) Рутинные задачи вызывают скуку и раздражение.",
                "Б) Легко справляюсь с рутинными задачами, если они разнообразны.",
                "В) Рутина не вызывает особых эмоций, просто выполняю то, что нужно.",
                "Г) Привык к рутинным делам, они дают чувство стабильности."
            )
        )
    )

    // Сопоставление вариантов ответа с типами темперамента
    val temperamentMap = mapOf(
        "А" to "Холерик",
        "Б" to "Сангвиник",
        "В" to "Флегматик",
        "Г" to "Меланхолик"
    )

    // Переменные для компонентов экрана вопросов
    private lateinit var questionLabel: JLabel
    private lateinit var optionsGroup: ButtonGroup
    private lateinit var optionsPanel: JPanel
    private lateinit var nextButton: JButton

    // Ссылка на текстовое поле экрана результатов
    private lateinit var resultTextArea: JTextArea

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(600, 400)
        setLocationRelativeTo(null)

        // Создаём панели-экраны
        val startPanel = createStartPanel()
        val questionPanel = createQuestionPanel()
        val resultPanel = createResultPanel()

        // Добавляем панели в контейнер с CardLayout
        mainPanel.add(startPanel, "start")
        mainPanel.add(questionPanel, "question")
        mainPanel.add(resultPanel, "result")
        add(mainPanel)
    }

    // Панель ввода ФИО
    private fun createStartPanel(): JPanel {
        val panel = JPanel(BorderLayout())
        val label = JLabel("Введите ваше ФИО:")
        label.horizontalAlignment = SwingConstants.CENTER
        val textField = JTextField(20)
        val button = JButton("Начать тест")

        button.addActionListener {
            fullName = textField.text.trim()
            if (fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Пожалуйста, введите ФИО", "Ошибка", JOptionPane.ERROR_MESSAGE)
            } else {
                // Сбрасываем предыдущие ответы и индекс вопроса
                answers.clear()
                currentQuestionIndex = 0
                updateQuestionPanel() // обновляем содержимое экрана вопросов
                cardLayout.show(mainPanel, "question")
            }
        }

        val inputPanel = JPanel()
        inputPanel.add(label)
        inputPanel.add(textField)
        inputPanel.add(button)
        panel.add(inputPanel, BorderLayout.CENTER)
        return panel
    }

    // Панель вопросов
    private fun createQuestionPanel(): JPanel {
        val panel = JPanel(BorderLayout())
        questionLabel = JLabel("Вопрос")
        questionLabel.horizontalAlignment = SwingConstants.CENTER
        panel.add(questionLabel, BorderLayout.NORTH)

        optionsPanel = JPanel()
        optionsPanel.layout = BoxLayout(optionsPanel, BoxLayout.Y_AXIS)
        panel.add(optionsPanel, BorderLayout.CENTER)

        nextButton = JButton("Далее")
        nextButton.addActionListener {
            // Определяем выбранный вариант ответа
            val selectedButton = optionsGroup.elements.toList().find { it.isSelected }
            if (selectedButton == null) {
                JOptionPane.showMessageDialog(this, "Пожалуйста, выберите вариант ответа", "Ошибка", JOptionPane.ERROR_MESSAGE)
                return@addActionListener
            }
            // Ответ имеет вид "А) Текст ответа" – берём первую букву
            val answerLetter = selectedButton.text.substring(0, 1)
            answers.add(answerLetter)
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                updateQuestionPanel()
            } else {
                showResultPanel()
            }
        }
        val bottomPanel = JPanel()
        bottomPanel.add(nextButton)
        panel.add(bottomPanel, BorderLayout.SOUTH)
        return panel
    }

    // Обновление содержимого панели вопросов
    private fun updateQuestionPanel() {
        val question = questions[currentQuestionIndex]
        questionLabel.text = question.questionText
        optionsPanel.removeAll()
        optionsGroup = ButtonGroup()
        for (option in question.options) {
            val radioButton = JRadioButton(option)
            optionsGroup.add(radioButton)
            optionsPanel.add(radioButton)
        }
        optionsPanel.revalidate()
        optionsPanel.repaint()
    }

    // Панель результатов
    private fun createResultPanel(): JPanel {
        val panel = JPanel(BorderLayout())
        resultTextArea = JTextArea(15, 50)
        resultTextArea.isEditable = false
        panel.add(JScrollPane(resultTextArea), BorderLayout.CENTER)

        val saveButton = JButton("Сохранить результат")
        saveButton.addActionListener {
            val resultText = resultTextArea.text
            try {
                val file = File("temperament_result.txt")
                file.writeText(resultText)
                JOptionPane.showMessageDialog(this, "Результаты сохранены в файл: ${file.absolutePath}")
            } catch (ex: Exception) {
                JOptionPane.showMessageDialog(this, "Ошибка при сохранении файла: ${ex.message}", "Ошибка", JOptionPane.ERROR_MESSAGE)
            }
        }
        val bottomPanel = JPanel()
        bottomPanel.add(saveButton)
        panel.add(bottomPanel, BorderLayout.SOUTH)
        return panel
    }

    // Расчёт и отображение результатов
    private fun showResultPanel() {
        // Подсчёт баллов для каждого типа
        val counts = mutableMapOf(
            "Холерик" to 0,
            "Сангвиник" to 0,
            "Флегматик" to 0,
            "Меланхолик" to 0
        )
        for (answer in answers) {
            // Сопоставляем букву с типом темперамента
            val type = when (answer) {
                "А" -> "Холерик"
                "Б" -> "Сангвиник"
                "В" -> "Флегматик"
                "Г" -> "Меланхолик"
                else -> ""
            }
            if (type.isNotEmpty()) {
                counts[type] = counts[type]!! + 1
            }
        }
        val totalQuestions = questions.size.toDouble()
        val percentages = counts.mapValues { (it.value / totalQuestions) * 100 }
        val maxCount = counts.values.maxOrNull() ?: 0
        val dominantTypes = counts.filter { it.value == maxCount }.keys
        val dominantType = if (dominantTypes.size == 1) {
            dominantTypes.first()
        } else {
            "Смешанный тип (${dominantTypes.joinToString(", ")})"
        }

        // Описания для каждого типа
        val descriptions = mapOf(
            "Холерик" to "Холерик – энергичный, решительный и импульсивный. Быстро принимает решения, но может быть нетерпеливым.",
            "Сангвиник" to "Сангвиник – общительный, оптимистичный и энергичный, легко адаптирующийся к новым ситуациям.",
            "Флегматик" to "Флегматик – спокойный, уравновешенный и надёжный, предпочитает стабильность и порядок.",
            "Меланхолик" to "Меланхолик – чувствительный, вдумчивый и склонный к глубоким переживаниям, имеет аналитический склад ума."
        )
        val resultDescription = if (!dominantType.startsWith("Смешанный")) {
            descriptions[dominantType] ?: ""
        } else {
            "Ваши ответы указывают на сочетание нескольких типов темперамента."
        }

        val analysisBuilder = StringBuilder()
        analysisBuilder.append("Анализ ответов:\n")
        for ((type, count) in counts) {
            val percent = String.format("%.1f", percentages[type])
            analysisBuilder.append("$type: $count ответ(ов) (${percent}%)\n")
        }
        analysisBuilder.append("\n")
        if (!dominantType.startsWith("Смешанный")) {
            analysisBuilder.append("Основной тип темперамента определён как $dominantType, так как количество ответов для этого типа наибольшее.\n")
        } else {
            analysisBuilder.append("Ваши ответы распределились равномерно между несколькими типами, что указывает на смешанный тип.\n")
        }

        val currentDate = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val formattedDate = currentDate.format(formatter)

        val resultText = """
            Результаты теста на определение темперамента
            ФИО: $fullName
            Дата: $formattedDate

            Основной тип темперамента: $dominantType
            Описание: $resultDescription

            $analysisBuilder
        """.trimIndent()

        resultTextArea.text = resultText
        cardLayout.show(mainPanel, "result")
    }
}

// Точка входа в приложение
fun main() {
    SwingUtilities.invokeLater {
        val app = TemperamentTestApp()
        app.isVisible = true
    }
}
