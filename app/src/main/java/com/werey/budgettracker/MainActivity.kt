package com.werey.budgettracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.werey.budgettracker.ui.theme.WereyBudgetTrackerTheme

private val Bg = Color(0xFF100D12); private val Card = Color(0xFF1B171E)
private val Plum = Color(0xFFD0A9FB); private val Ink = Color(0xFF3B2A52)
private val Text = Color(0xFFF4EEF6); private val Muted = Color(0xFFC9BFCC)
private val Green = Color(0xFFB9EF9B); private val Outline = Color(0xFF51485A)
private enum class Page { HOME, ADD }; private enum class Kind { FOOD, BUS, DATA, INCOME }
private data class Tx(val title:String, val category:String, val value:String, val kind:Kind, val income:Boolean=false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); enableEdgeToEdge()
        setContent { WereyBudgetTrackerTheme { App() } }
    }
}

@Composable private fun App() {
    var page by remember { mutableStateOf(Page.HOME) }; var requested by remember { mutableStateOf<Kind?>(null) }
    var entries by remember { mutableStateOf(listOf(Tx("Chicken & rice","Food","−₦3,200",Kind.FOOD), Tx("Bus to campus","Transport","−₦700",Kind.BUS), Tx("Salary","Income","+₦85,000",Kind.INCOME,true))) }
    fun open(kind:Kind?) { requested=kind; page=Page.ADD }
    Box(Modifier.fillMaxSize().background(Bg)) {
        if(page==Page.HOME) Home(entries, ::open)
        else Add(requested, { page=Page.HOME }) { entries=listOf(it)+entries; page=Page.HOME }
    }
}

@Composable private fun Home(entries:List<Tx>, open:(Kind?)->Unit) {
    LazyColumn(Modifier.fillMaxSize(), contentPadding=PaddingValues(22.dp,44.dp,22.dp,110.dp)) {
        item {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Column { Text("Good morning, Kim",color=Text,fontSize=25.sp,fontWeight=FontWeight.Bold); Spacer(Modifier.height(4.dp)); Text("Your money, clearly planned.",color=Muted,fontSize=13.sp) }
                Box(Modifier.size(46.dp).clip(CircleShape).background(Plum),contentAlignment=Alignment.Center) { Text("K",color=Ink,fontSize=16.sp,fontWeight=FontWeight.Bold) }
            }
            Spacer(Modifier.height(22.dp)); Balance(); Spacer(Modifier.height(20.dp)); Heading("Quick add"); Spacer(Modifier.height(14.dp))
            Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)) {
                Quick("Food","₦1,200",Kind.FOOD,Modifier.weight(1f),open); Quick("Transport","₦500",Kind.BUS,Modifier.weight(1f),open); Quick("Data","₦2,000",Kind.DATA,Modifier.weight(1f),open)
            }
            Spacer(Modifier.height(26.dp)); Row(Modifier.fillMaxWidth(),Arrangement.SpaceBetween) { Heading("Today"); Text("See all",color=Plum,fontSize=12.sp,fontWeight=FontWeight.Bold) }; Spacer(Modifier.height(12.dp))
        }
        items(entries.size) { i -> Entry(entries[i]); if(i<entries.lastIndex) Spacer(Modifier.height(8.dp)) }
    }
    Row(Modifier.fillMaxWidth().padding(22.dp,0.dp,22.dp,30.dp).height(64.dp),horizontalArrangement=Arrangement.spacedBy(12.dp),verticalAlignment=Alignment.CenterVertically) {
        Row(Modifier.weight(1f).fillMaxHeight().clip(RoundedCornerShape(32.dp)).background(Card).padding(horizontal=8.dp),horizontalArrangement=Arrangement.SpaceEvenly,verticalAlignment=Alignment.CenterVertically) {
            Nav("⌂","Home",true); Nav("▱","Plan",false); Nav("⌁","Insights",false)
        }
        Box(Modifier.size(56.dp).clip(CircleShape).background(Plum).clickable(role=Role.Button){open(null)}.semantics{contentDescription="Add transaction"},contentAlignment=Alignment.Center) { Text("+",color=Ink,fontSize=31.sp) }
    }
}
@Composable private fun Balance() = Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(28.dp)).background(Plum).padding(22.dp,21.dp)) {
    Text("AVAILABLE TO SPEND",color=Ink,fontSize=11.sp,fontWeight=FontWeight.Bold); Spacer(Modifier.height(10.dp)); Text("₦42,550",color=Ink,fontSize=39.sp,fontWeight=FontWeight.ExtraBold)
    Spacer(Modifier.height(5.dp)); Text("until 30 October",color=Ink,fontSize=13.sp); Spacer(Modifier.height(16.dp))
    Row(Modifier.fillMaxWidth().height(10.dp).clip(CircleShape).background(Ink)) { Spacer(Modifier.fillMaxWidth(.63f).fillMaxHeight().clip(CircleShape).background(Green)) }
}
@Composable private fun Quick(title:String,value:String,kind:Kind,modifier:Modifier,open:(Kind?)->Unit) = Column(modifier.clip(RoundedCornerShape(22.dp)).background(Card).clickable(role=Role.Button){open(kind)}.padding(16.dp)) {
    Symbol(kind,Plum,20.sp); Spacer(Modifier.height(8.dp)); Text(title,color=Text,fontSize=12.sp,fontWeight=FontWeight.Bold,maxLines=1); Spacer(Modifier.height(3.dp)); Text(value,color=Muted,fontSize=11.sp,maxLines=1)
}
@Composable private fun Entry(tx:Tx) = Row(Modifier.fillMaxWidth().height(64.dp).clip(RoundedCornerShape(20.dp)).background(Card).padding(horizontal=13.dp),verticalAlignment=Alignment.CenterVertically) {
    Box(Modifier.size(40.dp).clip(CircleShape).background(Ink),contentAlignment=Alignment.Center) { Symbol(tx.kind,Plum,18.sp) }
    Spacer(Modifier.width(12.dp)); Column(Modifier.weight(1f)) { Text(tx.title,color=Text,fontSize=13.sp,fontWeight=FontWeight.Bold); Text(tx.category,color=Muted,fontSize=11.sp) }
    Text(tx.value,color=if(tx.income)Green else Text,fontSize=14.sp,fontWeight=FontWeight.Bold)
}
@Composable private fun Nav(icon:String,label:String,selected:Boolean) = Column(horizontalAlignment=Alignment.CenterHorizontally) {
    Box(Modifier.size(56.dp,30.dp).clip(RoundedCornerShape(20.dp)).background(if(selected)Ink else Color.Transparent),contentAlignment=Alignment.Center) { Text(icon,color=if(selected)Plum else Muted,fontSize=18.sp) }
    Text(label,color=if(selected)Plum else Muted,fontSize=9.sp,fontWeight=if(selected)FontWeight.Bold else FontWeight.Normal)
}

@Composable private fun Add(initial:Kind?,back:()->Unit,save:(Tx)->Unit) {
    var income by remember { mutableStateOf(initial==Kind.INCOME) }; var amount by remember { mutableStateOf(amountFor(initial)) }
    val title=if(income)"Salary" else when(initial){Kind.BUS->"Bus to campus";Kind.DATA->"Mobile data";else->"Lunch and drink"}
    val category=if(income)"Income" else if(initial==Kind.BUS)"Transport" else "Food"
    Column(Modifier.fillMaxSize().padding(22.dp,44.dp,22.dp,26.dp)) {
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically) {
            Circle("‹","Back",back); Text("Add transaction",Modifier.weight(1f),color=Text,textAlign=TextAlign.Center,fontSize=21.sp,fontWeight=FontWeight.Bold); Spacer(Modifier.size(48.dp))
        }
        Spacer(Modifier.height(16.dp)); Row(Modifier.fillMaxWidth().height(48.dp).clip(RoundedCornerShape(28.dp)).background(Card)) { Tab("Expense",!income,Modifier.weight(1f)){income=false}; Tab("Income",income,Modifier.weight(1f)){income=true} }
        Spacer(Modifier.height(23.dp)); Text("AMOUNT",Modifier.fillMaxWidth(),color=Muted,textAlign=TextAlign.Center,fontSize=11.sp,fontWeight=FontWeight.Bold); Spacer(Modifier.height(8.dp))
        Text("₦"+amount,Modifier.fillMaxWidth(),color=Text,textAlign=TextAlign.Center,fontSize=42.sp,fontWeight=FontWeight.ExtraBold); Spacer(Modifier.height(3.dp)); Text("Tap the amount to edit",Modifier.fillMaxWidth(),color=Muted,textAlign=TextAlign.Center,fontSize=12.sp)
        Spacer(Modifier.height(19.dp)); Detail(title,income); Spacer(Modifier.height(18.dp)); Text("Pinned",color=Muted,fontSize=12.sp,fontWeight=FontWeight.Bold); Spacer(Modifier.height(10.dp))
        Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(8.dp)) { listOf("Fanta ₦500","Lunch ₦2,500","Bus ₦700").forEach { pin -> Box(Modifier.weight(1f).height(36.dp).clip(RoundedCornerShape(20.dp)).background(Ink).clickable{amount=format(pin.filter(Char::isDigit))},contentAlignment=Alignment.Center){Text(pin,color=Plum,fontSize=10.sp,fontWeight=FontWeight.Bold,maxLines=1)} } }
        Spacer(Modifier.height(15.dp)); Keypad { amount=changeAmount(amount,it) }; Spacer(Modifier.weight(1f))
        Box(Modifier.fillMaxWidth().height(56.dp).clip(RoundedCornerShape(30.dp)).background(Plum).clickable(role=Role.Button) { save(Tx(title,category,(if(income)"+₦" else "−₦")+amount,if(income)Kind.INCOME else initial?:Kind.FOOD,income)) },contentAlignment=Alignment.Center) { Text(if(income)"Save income" else "Save expense",color=Ink,fontSize=14.sp,fontWeight=FontWeight.Bold) }
    }
}
@Composable private fun Circle(symbol:String,description:String,action:()->Unit) = Box(Modifier.size(48.dp).clip(CircleShape).background(Card).clickable(role=Role.Button,onClick=action).semantics{contentDescription=description},contentAlignment=Alignment.Center){Text(symbol,color=Text,fontSize=33.sp)}
@Composable private fun Tab(label:String,selected:Boolean,modifier:Modifier,action:()->Unit) = Box(modifier.padding(4.dp).clip(RoundedCornerShape(25.dp)).background(if(selected)Plum else Color.Transparent).clickable(role=Role.Tab,onClick=action),contentAlignment=Alignment.Center){Text(label,color=if(selected)Ink else Muted,fontSize=13.sp,fontWeight=FontWeight.Bold)}
@Composable private fun Detail(title:String,income:Boolean) = Row(Modifier.fillMaxWidth().height(59.dp).clip(RoundedCornerShape(19.dp)).background(Card).padding(horizontal=13.dp),verticalAlignment=Alignment.CenterVertically) {
    Box(Modifier.size(38.dp).clip(CircleShape).background(Ink),contentAlignment=Alignment.Center){Symbol(if(income)Kind.INCOME else Kind.FOOD,Plum,18.sp)}; Spacer(Modifier.width(10.dp)); Column {Text("What was this for?",color=Muted,fontSize=11.sp,fontWeight=FontWeight.Bold);Text(title,color=Text,fontSize=14.sp,fontWeight=FontWeight.SemiBold)}
}
@Composable private fun Keypad(action:(String)->Unit) = Column(verticalArrangement=Arrangement.spacedBy(10.dp)) { listOf("1","2","3","4","5","6","7","8","9",".","0","⌫").chunked(3).forEach { row -> Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.spacedBy(10.dp)) { row.forEach { key -> Box(Modifier.weight(1f).height(56.dp).clip(RoundedCornerShape(18.dp)).background(Card).clickable(role=Role.Button){action(if(key=="⌫")"delete" else key)}.semantics{contentDescription=if(key=="⌫")"Delete last digit" else key},contentAlignment=Alignment.Center){Text(key,color=Text,fontSize=if(key=="⌫")20.sp else 23.sp,fontWeight=FontWeight.SemiBold)} } } } }
private fun amountFor(kind:Kind?)=when(kind){Kind.FOOD->"1,200";Kind.BUS->"500";Kind.DATA->"2,000";Kind.INCOME->"85,000";null->"2,800"}
private fun format(raw:String)=raw.reversed().chunked(3).joinToString(",").reversed().ifBlank{"0"}
private fun changeAmount(current:String,key:String):String { val raw=current.replace(",",""); val next=when(key){"delete"->raw.dropLast(1);"."->if(raw.contains("."))raw else raw+".";else->if(raw=="0")key else raw+key}; val parts=next.split("."); return if(parts.size>1)format(parts[0])+"."+parts[1] else format(next) }
@Composable private fun Heading(label:String)=Text(label,color=Text,fontSize=20.sp,fontWeight=FontWeight.Bold)
@Composable private fun Symbol(kind:Kind,color:Color,size:androidx.compose.ui.unit.TextUnit) { Text(when(kind){Kind.FOOD->"♜";Kind.BUS->"▣";Kind.DATA->"▥";Kind.INCOME->"+"},color=color,fontSize=size,fontWeight=FontWeight.Bold) }
@Preview(device="spec:width=393dp,height=852dp,dpi=440") @Composable private fun PreviewApp(){WereyBudgetTrackerTheme{App()}}
