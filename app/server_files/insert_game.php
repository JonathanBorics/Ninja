<?php
$servername = "localhost";
$username = "root"; // Adatbázis felhasználónév
$password = ""; // Adatbázis jelszó
$dbname = "number_ninjas"; // Adatbázis neve

// Kapcsolat létrehozása
$conn = new mysqli($servername, $username, $password, $dbname);

// Kapcsolódási hiba ellenőrzése
if ($conn->connect_error) {
    die(json_encode(array("status" => "error", "message" => "Kapcsolódási hiba: " . $conn->connect_error)));
}

// POST adatok fogadása
$player_id = $_POST['player_id'] ?? null;
$mode = $_POST['mode'] ?? null;
$score = $_POST['score'] ?? null;
$time = $_POST['time'] ?? null;

// Ellenőrzés: hiányzó adatok
if (!$player_id || !$mode || !$score || !$time) {
    echo json_encode(array("status" => "error", "message" => "Hiányzó adatok: player_id, mode, score vagy time"));
    exit();
}

// Adatok beszúrása a Game táblába
$sql = "INSERT INTO Game (player_id, mode, score, time) VALUES (?, ?, ?, ?)";
$stmt = $conn->prepare($sql);

if ($stmt) {
    $stmt->bind_param("isii", $player_id, $mode, $score, $time); // "isii" -> integer, string, integer, integer
    if ($stmt->execute()) {
        echo json_encode(array("status" => "success", "message" => "Játék sikeresen hozzáadva", "game_id" => $stmt->insert_id));
    } else {
        echo json_encode(array("status" => "error", "message" => "Beszúrás sikertelen: " . $stmt->error));
    }
    $stmt->close();
} else {
    echo json_encode(array("status" => "error", "message" => "SQL előkészítés sikertelen: " . $conn->error));
}

$conn->close();
?>
