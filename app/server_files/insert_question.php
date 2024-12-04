<?php
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "number_ninjas";

$conn = new mysqli($servername, $username, $password, $dbname);

if ($conn->connect_error) {
    die(json_encode(array("status" => "error", "message" => "Kapcsolódási hiba: " . $conn->connect_error)));
}

// POST adatok fogadása
$game_id = $_POST['game_id'] ?? null;
$question_text = $_POST['question_text'] ?? null;
$correct_answer = $_POST['correct_answer'] ?? null;
$player_answer = $_POST['player_answer'] ?? null;
$is_correct = $_POST['is_correct'] ?? null;

// Ellenőrzés: hiányzó adatok
if (!$game_id || !$question_text || !$correct_answer) {
    echo json_encode(array("status" => "error", "message" => "Hiányzó adatok: game_id, question_text vagy correct_answer"));
    exit();
}

// Adatok beszúrása a Questions táblába
$sql = "INSERT INTO Questions (game_id, question_text, correct_answer, player_answer, is_correct) VALUES (?, ?, ?, ?, ?)";
$stmt = $conn->prepare($sql);

if ($stmt) {
    $stmt->bind_param("isddi", $game_id, $question_text, $correct_answer, $player_answer, $is_correct); // "isddi" -> integer, string, double, double, integer
    if ($stmt->execute()) {
        echo json_encode(array("status" => "success", "message" => "Kérdés sikeresen hozzáadva"));
    } else {
        echo json_encode(array("status" => "error", "message" => "Beszúrás sikertelen: " . $stmt->error));
    }
    $stmt->close();
} else {
    echo json_encode(array("status" => "error", "message" => "SQL előkészítés sikertelen: " . $conn->error));
}

$conn->close();
?>
