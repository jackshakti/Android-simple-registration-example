<?php
$host = "localhost";
$user = "root"; // change as per your server
$pass = "";
$db = "userdb";

$conn = new mysqli($host, $user, $pass, $db);

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $name = $_POST["name"] ?? '';
    $phone = $_POST["phone"] ?? '';
    $address = $_POST["address"] ?? '';

    if ($name && $phone && $address) {
        $stmt = $conn->prepare("INSERT INTO users (name, phone, address) VALUES (?, ?, ?)");
        $stmt->bind_param("sss", $name, $phone, $address);
        if ($stmt->execute()) {
            echo json_encode(["status" => "success", "message" => "Registered successfully"]);
        } else {
            echo json_encode(["status" => "error", "message" => "Database error"]);
        }
        $stmt->close();
    } else {
        echo json_encode(["status" => "error", "message" => "All fields required"]);
    }
}

$conn->close();
?>
