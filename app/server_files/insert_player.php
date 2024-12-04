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
$name = $_POST['name'] ?? null;

// Ellenőrzés: hiányzó név
if (!$name) {
    echo json_encode(array("status" => "error", "message" => "Hiányzó adat: name"));
    exit();
}

// Adatok beszúrása a Players táblába
$sql = "INSERT INTO Players (name) VALUES (?)";
$stmt = $conn->prepare($sql);

if ($stmt) {
    $stmt->bind_param("s", $name); // "s" -> string
    if ($stmt->execute()) {
        echo json_encode(array("status" => "success", "message" => "Játékos sikeresen hozzáadva", "player_id" => $stmt->insert_id));
    } else {
        echo json_encode(array("status" => "error", "message" => "Beszúrás sikertelen: " . $stmt->error));
    }
    $stmt->close();
} else {
    echo json_encode(array("status" => "error", "message" => "SQL előkészítés sikertelen: " . $conn->error));
}

$conn->close();
?>
